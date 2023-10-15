package com.example.passportphotocomparisonthesis.Utils.JSON

import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.Country
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JsonParser() {

    private val gson = Gson()

    suspend fun parseCountries(jsonString: String): List<Country> {
        return withContext(Dispatchers.IO) {
            gson.fromJson(jsonString, Array<Country>::class.java).toList()
        }
    }
}