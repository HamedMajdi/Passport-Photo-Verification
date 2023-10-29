package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import com.example.passportphotocomparisonthesis.databinding.FragmentAddManuallyBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddManuallyFragment : Fragment() {

    private lateinit var binding: FragmentAddManuallyBinding
    private lateinit var coroutineScope: CoroutineScope

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


        binding.editTextBirthDate.setOnClickListener {
            showDatePickerDialog {
                it
                binding.TILBirth.error = null
                binding.editTextBirthDate.setText(it)
            }
        }

        binding.editTextExpirationDate.setOnClickListener {
            showDatePickerDialog {
                it
                binding.TILExpiration.error = null
                binding.editTextExpirationDate.setText(it)
            }
        }

        binding.buttonNextDataPiece.setOnClickListener {

            val birthCheck = checkBirthDateEditTextValueEntered()
            val expirationCheck = checkExpirationDateEditTextValueEntered()
            val documentCheck = checkDocumentNumberDigitCountValidity()

            if (birthCheck && expirationCheck && documentCheck){
                binding.layoutDataPart1.visibility = View.GONE
                binding.layoutDataPart2.visibility = View.VISIBLE
            }
        }
    }

    private fun areAllNecessaryFieldsFilled(): Boolean {
        return checkBirthDateEditTextValueEntered() && checkExpirationDateEditTextValueEntered() && checkDocumentNumberDigitCountValidity()
    }


    private fun checkBirthDateEditTextValueEntered(): Boolean {
        if (binding.editTextBirthDate.text.isNullOrBlank()) {
            binding.TILBirth.error = "Birth Date Must Be Selected"
            return false
        } else{
            binding.TILBirth.error = null
            return true
        }
    }

    private fun checkExpirationDateEditTextValueEntered(): Boolean {
        if (binding.editTextExpirationDate.text.isNullOrBlank()) {
            binding.TILExpiration.error = "Expiration Date Must Be Selected"
            return false
        }else{
            binding.TILExpiration.error = null
            return true
        }
    }

    private fun checkDocumentNumberDigitCountValidity(): Boolean {
        if (binding.editTextDocumentNumber.text.isNullOrBlank() || binding.editTextDocumentNumber.length() != 9){
            binding.TILDocument.error = "Document Number Must be 9 Characters"
            return false
        }
        else{
            binding.TILDocument.error = null
            return true
        }
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
//        binding.spinnerGender.adapter = countryAdapter
//        binding.spinnerGender.adapter = countryAdapter

//        binding.spinnerGender.selectedItem as SpinnerData
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