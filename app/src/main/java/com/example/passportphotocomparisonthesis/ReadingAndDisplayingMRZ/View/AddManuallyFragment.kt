package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.IconGenerator.GenderImageGenerator
import com.example.passportphotocomparisonthesis.databinding.FragmentAddManuallyBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddManuallyFragment : Fragment() {

    private lateinit var binding: FragmentAddManuallyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var userViewModel: UserBACVeiwModel

    //    private lateinit var countriesDataForSpinner: List<SpinnerData>
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
            hideKeyboard()
        }

        binding.editTextDocumentNumber.doOnTextChanged { text, start, before, count ->
            setNextPageButtonOpacity()
        }

        binding.editTextBirthDate.setOnClickListener {
            hideKeyboard()
            showDatePickerDialog {
                binding.TILBirth.error = null
                binding.editTextBirthDate.setText(it)
                setNextPageButtonOpacity()
            }
        }

        binding.editTextExpirationDate.setOnClickListener {
            hideKeyboard()
            showDatePickerDialog {
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
            (binding.spinnerCountry.selectedItem as SpinnerData).text

            val userBAC = UserBAC(
                binding.editTextDocumentNumber.text.toString(),
                DateParser.parseDateFromSlashFormatToRaw(binding.editTextExpirationDate.text.toString())!!,
                DateParser.parseDateFromSlashFormatToRaw(binding.editTextBirthDate.text.toString())!!,
                binding.editTextName.text.toString(),
                "M",
                "IRAN",
                "IR",
                3
            )

            userViewModel.addUser(userBAC)
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

    private fun hideKeyboard(){
        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showDatePickerDialog(onDatePicked: (String) -> Unit) {
        val myCalendar: Calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${monthOfYear + 1}/$year"
                onDatePicked(selectedDate)
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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

}