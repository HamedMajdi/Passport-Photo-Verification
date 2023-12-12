package com.example.passportphotocomparisonthesis.Utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View.SpinnerAdapter
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import kotlin.math.log

fun hideKeyboard(context: Context, fragmentActivity: FragmentActivity) {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = fragmentActivity.currentFocus ?: View(fragmentActivity)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showDatePickerDialog(context: Context, onDatePicked: (String) -> Unit) {
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

fun displayErrorMessageOnInputLayout(view: TextInputLayout, message: String?, isValid: Boolean) {
    if (isValid)
        view.error = null
    else
        view.error = message
}


object SpinnerUtils {

    fun loadGenderSpinner(spinner: Spinner, context: Context) {
        val genderDataForSpinner = StaticDataRepository.getGenderOptions(context)
        val genderAdapter = SpinnerAdapter(context, genderDataForSpinner)
        spinner.adapter = genderAdapter
    }

    fun loadDocumentTypeSpinner(spinner: Spinner, context: Context) {
        val documentDataForSpinner = StaticDataRepository.getDocumentTypeOptions(context)
        val documentAdapter = SpinnerAdapter(context, documentDataForSpinner)
        spinner.adapter = documentAdapter
    }

    suspend fun loadCountrySpinner(spinner: Spinner, context: Context) {
        val countriesDataForSpinner = StaticDataRepository.getCountryOptions(context)
        val countryAdapter = SpinnerAdapter(context, countriesDataForSpinner)
        spinner.adapter = countryAdapter
    }

    fun getGender(spinner: Spinner): String? {
        when (spinner.selectedItemPosition) {
            1 -> return "M"
            2 -> return "F"
            3 -> return "<"
            else -> return null
        }
    }

    fun getCountryFullName(spinner: Spinner): String? {
        if (spinner.selectedItemPosition != 0) {
            val selectedCountry =
                spinner.selectedItem as com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
            return selectedCountry.text
        }
        return null
    }

    suspend fun getCountryAlpha2Name(context: Context, spinner: Spinner): String? {
        val selectedCountry = spinner.selectedItem as SpinnerData
        val repository = CountryRepository(JsonReader(), JsonParser())
        var countryAlpha2: String? = null

        countryAlpha2 = repository.getCountryByFullName(selectedCountry.text, context)?.alpha2

        return countryAlpha2
    }

    fun getDocumentType(spinner: Spinner): Int? {
        when (spinner.selectedItemPosition) {
            1 -> return 1
            2 -> return 3
            else -> return null
        }
    }

    fun setGender(spinner: Spinner, genderValue: String?, context: Context) {

        val genderOptions = StaticDataRepository.getGenderOptions(context)

        val position = genderOptions.indexOfFirst {
            it.text == when (genderValue) {
                "M" -> context.getString(R.string.male)
                "F" -> context.getString(R.string.female)
                "<" -> context.getString(R.string.other)
                else -> context.getString(R.string.select_gender)
            }
        }
        spinner.setSelection(position)
    }

    fun setDocumentType(spinner: Spinner, documentValue: Int?, context: Context) {

        val documentOptions = StaticDataRepository.getDocumentTypeOptions(context)
        val position = documentOptions.indexOfFirst {
            it.text == when (documentValue) {
                1 -> context.getString(R.string.id_card)
                2 -> context.getString(R.string.id_card)
                3 -> context.getString(R.string.passport)
                else -> context.getString(R.string.select_document_type)
            }
        }
        spinner.setSelection(position)
    }

    suspend fun setCountry(spinner: Spinner, countryValue: String?, context: Context) {
        val countryOptions = StaticDataRepository.getCountryOptions(context)
        var position = countryOptions.indexOfFirst {
            val countryName = it.text.lowercase()
            countryName == countryValue?.lowercase() ?: context.getString(R.string.select_a_country)
        }
        if (position==-1)
            position=0
        spinner.setSelection(position)

    }
}