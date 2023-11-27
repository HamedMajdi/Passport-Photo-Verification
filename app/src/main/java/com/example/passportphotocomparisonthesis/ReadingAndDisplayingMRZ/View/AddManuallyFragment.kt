package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.ViewUtils
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import com.example.passportphotocomparisonthesis.Utils.hideKeyboard
import com.example.passportphotocomparisonthesis.Utils.showDatePickerDialog
import com.example.passportphotocomparisonthesis.databinding.FragmentAddManuallyBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class AddManuallyFragment : Fragment() {

    private lateinit var binding: FragmentAddManuallyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var userViewModel: UserBACVeiwModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddManuallyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCountryTypeSpinner()
        loadDocumentTypeSpinner()
        loadGenderSpinner()

        view.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
        }

        binding.editTextDocumentNumber.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrBlank() && text.length==9)
                binding.TILDocument.error = null
            setNextPageButtonOpacity()
        }

        binding.editTextBirthDate.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
            showDatePickerDialog(requireContext()) {
                binding.TILBirth.error = null
                binding.editTextBirthDate.setText(it)
                setNextPageButtonOpacity()
            }
        }

        binding.editTextExpirationDate.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
            showDatePickerDialog(requireContext()) {
                binding.TILExpiration.error = null
                binding.editTextExpirationDate.setText(it)
                setNextPageButtonOpacity()
            }
        }

        binding.buttonNextDataPiece.setOnClickListener {

            val birthCheck = checkBirthDateEditTextValueEntered()
            setErrorMessage(binding.TILBirth, "Birth Date Must Be Selected", birthCheck)

            val expirationCheck = checkExpirationDateEditTextValueEntered()
            setErrorMessage(binding.TILExpiration, "Expiration Date Must Be Selected", expirationCheck)

            val documentCheck = checkDocumentNumberDigitCountValidity()
            setErrorMessage(binding.TILDocument, "Document Number Must be 9 Characters", documentCheck)

            if (birthCheck && expirationCheck && documentCheck){
                binding.layoutDataPart1.visibility = View.GONE
                binding.layoutDataPart2.visibility = View.VISIBLE
            }
        }

        binding.buttonPreviousDataPiece.setOnClickListener {
            binding.layoutDataPart2.visibility = View.GONE
            binding.layoutDataPart1.visibility = View.VISIBLE
        }

        binding.buttonAddToDocumentListDatabase.setOnClickListener {
            userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)

            val userBAC = UserBAC(
                binding.editTextDocumentNumber.text.toString(),
                DateParser.parseDateFromSlashFormatToRaw(binding.editTextExpirationDate.text.toString())!!,
                DateParser.parseDateFromSlashFormatToRaw(binding.editTextBirthDate.text.toString())!!,
                binding.editTextName.text.toString(),
                getGender(),
                getCountryFullName(),
                getCountryAlpha2Name(),
                getDocumentType()
            )

            userViewModel.addUser(userBAC)
            val action = AddDocumentFragmentDirections.actionAddDocumentFragmentToUserMRZFragment(userBAC)
            findNavController().navigate(action)

        }
    }


    private fun areAllNecessaryFieldsFilled(): Boolean {
        return checkBirthDateEditTextValueEntered() && checkExpirationDateEditTextValueEntered() && checkDocumentNumberDigitCountValidity()
    }

    private fun checkBirthDateEditTextValueEntered(): Boolean {
        return !binding.editTextBirthDate.text.isNullOrBlank()
    }

    private fun checkExpirationDateEditTextValueEntered(): Boolean {
        return !binding.editTextExpirationDate.text.isNullOrBlank()
    }


    private fun checkDocumentNumberDigitCountValidity(): Boolean {
        return !(binding.editTextDocumentNumber.text.isNullOrBlank() || binding.editTextDocumentNumber.length() != 9)
    }

    private fun setErrorMessage(view: TextInputLayout, message: String, isValid: Boolean){
        if (isValid)
            view.error = null
        else
            view.error = message
    }

    private fun setNextPageButtonOpacity(){
        if (areAllNecessaryFieldsFilled())
            binding.buttonNextDataPiece.alpha = 1f
        else
            binding.buttonNextDataPiece.alpha = 0.3f
    }


    private fun loadGenderSpinner() {
        val genderDataForSpinner = StaticDataRepository.getGenderOptions()
        val countryAdapter = SpinnerAdapter(requireContext(), genderDataForSpinner)
        binding.spinnerGender.adapter = countryAdapter
    }

    private fun loadDocumentTypeSpinner() {
        val documentDataForSpinner = StaticDataRepository.getDocumentTypeOptions()
        val countryAdapter = SpinnerAdapter(requireContext(), documentDataForSpinner)
        binding.spinnerDocumentType.adapter = countryAdapter
    }

    private fun loadCountryTypeSpinner() {
        coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            val countriesDataForSpinner = StaticDataRepository.getCountryOptions(requireContext())
            val countryAdapter = SpinnerAdapter(requireContext(), countriesDataForSpinner)
            binding.spinnerCountry.adapter = countryAdapter
        }

    }

    private fun getGender(): String?{
        when (binding.spinnerGender.selectedItemPosition){
            1 -> return "M"
            2 -> return "F"
            3 -> return "<"
            else -> return null
        }
    }

    private fun getCountryFullName(): String? {
        if (binding.spinnerCountry.selectedItemPosition != 0){
            val selectedCountry = binding.spinnerCountry.selectedItem as com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
            return selectedCountry.text
        }
        return null
    }

    private fun getCountryAlpha2Name(): String? {
        val selectedCountry = binding.spinnerCountry.selectedItem as SpinnerData
        val repository = CountryRepository(JsonReader(), JsonParser())
        var countryAlpha2: String? = null

        runBlocking {
            countryAlpha2 = repository.getCountryByFullName(selectedCountry.text, requireContext())?.alpha2
        }

        return countryAlpha2
    }

    private fun getDocumentType(): Int?{
        when (binding.spinnerGender.selectedItemPosition){
            1 -> return 1
            2 -> return 3
            else -> return null
        }
    }
}