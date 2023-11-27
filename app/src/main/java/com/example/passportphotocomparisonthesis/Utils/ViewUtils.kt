package com.example.passportphotocomparisonthesis.Utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View.SpinnerAdapter
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

fun hideKeyboard(context: Context, fragmentActivity: FragmentActivity){
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = fragmentActivity.currentFocus ?: View(fragmentActivity)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showDatePickerDialog(context: Context,onDatePicked: (String) -> Unit) {
    val myCalendar: Calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${monthOfYear + 1}/$year"
            onDatePicked(selectedDate)
        },
        myCalendar.get(Calendar.YEAR),
        myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

object SpinnerUtils {

    fun loadGenderSpinner(spinner: Spinner, context: Context) {
        val genderDataForSpinner = StaticDataRepository.getGenderOptions()
        val genderAdapter = SpinnerAdapter(context, genderDataForSpinner)
        spinner.adapter = genderAdapter
    }

    fun loadDocumentTypeSpinner(spinner: Spinner, context: Context) {
        val documentDataForSpinner = StaticDataRepository.getDocumentTypeOptions()
        val documentAdapter = SpinnerAdapter(context, documentDataForSpinner)
        spinner.adapter = documentAdapter
    }

    private fun loadCountryTypeSpinner(coroutineScope: CoroutineScope, spinner: Spinner, context: Context) {

        coroutineScope.launch {
            val countriesDataForSpinner = StaticDataRepository.getCountryOptions(context)
            val countryAdapter = SpinnerAdapter(context, countriesDataForSpinner)
            spinner.adapter = countryAdapter
        }

    }

    fun getGender(spinner: Spinner): String?{
        when (spinner.selectedItemPosition){
            1 -> return "M"
            2 -> return "F"
            3 -> return "<"
            else -> return null
        }
    }

    private fun getCountryFullName(spinner: Spinner): String? {
        if (spinner.selectedItemPosition != 0){
            val selectedCountry = spinner.selectedItem as com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
            return selectedCountry.text
        }
        return null
    }

    private fun getCountryAlpha2Name(context: Context, spinner: Spinner): String? {
        val selectedCountry = spinner.selectedItem as SpinnerData
        val repository = CountryRepository(JsonReader(), JsonParser())
        var countryAlpha2: String? = null

        runBlocking {
            countryAlpha2 = repository.getCountryByFullName(selectedCountry.text, context)?.alpha2
        }

        return countryAlpha2
    }

    private fun getDocumentType(spinner: Spinner): Int?{
        when (spinner.selectedItemPosition){
            1 -> return 1
            2 -> return 3
            else -> return null
        }
    }

}