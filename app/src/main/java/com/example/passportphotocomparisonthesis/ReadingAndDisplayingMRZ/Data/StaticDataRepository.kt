package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data

import android.content.Context
import android.util.Log
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader

class StaticDataRepository {

    companion object {

        fun getGenderOptions(): List<SpinnerData> {
            return listOf(
                SpinnerData(R.drawable.ic_gender, "Select Gender"),
                SpinnerData(R.drawable.ic_male, "Male"),
                SpinnerData(R.drawable.ic_female, "Female"),
                SpinnerData(R.drawable.ic_non_binary, "Other")
            )
        }

        fun getDocumentTypeOptions(): List<SpinnerData> {
            return listOf(
                SpinnerData(R.drawable.ic_document, "Select Document Type"),
                SpinnerData(R.drawable.ic_id_card, "ID Card"),
                SpinnerData(R.drawable.ic_passport, "Passport")
            )
        }

        suspend fun getCountryOptions(context: Context): List<SpinnerData> {
            val repository = CountryRepository(JsonReader(), JsonParser())
            val countries = repository.getAllCountries(context)
            val countryList =  countries.map { country ->
                val imageName = if (country.alpha2.lowercase() == "do") "do1" else country.alpha2.lowercase()

                val imageResId = context.resources.getIdentifier("$imageName", "drawable", context.packageName)

                SpinnerData(imageResId, country.name)
            }

            return listOf(SpinnerData(R.drawable.ic_country, "Select a country")) + countryList

        }
    }
}