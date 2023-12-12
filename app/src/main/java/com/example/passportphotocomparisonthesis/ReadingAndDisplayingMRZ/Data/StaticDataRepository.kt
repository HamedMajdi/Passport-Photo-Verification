package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data

import android.content.Context
import android.util.Log
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader

class StaticDataRepository {

    companion object {

        fun getGenderOptions(context: Context): List<SpinnerData> {
            return listOf(
                SpinnerData(R.drawable.ic_gender, context.getString(R.string.select_gender)),
                SpinnerData(R.drawable.ic_male, context.getString(R.string.male)),
                SpinnerData(R.drawable.ic_female, context.getString(R.string.female)),
                SpinnerData(R.drawable.ic_non_binary, context.getString(R.string.other))
            )
        }

        fun getDocumentTypeOptions(context: Context): List<SpinnerData> {
            return listOf(
                SpinnerData(R.drawable.ic_document, context.getString(R.string.select_document_type)),
                SpinnerData(R.drawable.ic_id_card, context.getString(R.string.id_card)),
                SpinnerData(R.drawable.ic_passport, context.getString(R.string.passport))
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

            return listOf(SpinnerData(R.drawable.ic_country, context.getString(R.string.select_a_country))) + countryList
        }
    }
}