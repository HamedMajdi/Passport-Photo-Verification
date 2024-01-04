package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data

import android.content.Context
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.Country
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.CountryDao
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader

/*
Repository: On the other hand, the Repository pattern is a kind of container where data access logic is stored.
It hides the details of data access logic from business logic.
In other words, business logic can access the data object without having knowledge of the underlying data access architecture.

The Repository pattern is a popular way to achieve separation between the physical database, queries and other data access logic from the rest of an application.
The repository provides a collection interface to access data stored in a database, file system or external service.
Data is read from the repository and returned to a client as a business object.
Data is also received from the client as a business object and stored in the repository.


 */
class CountryRepository(
    private val jsonReader: JsonReader,
    private val jsonParser: JsonParser
) : CountryDao {

    private var countries: List<Country>? = null

    override suspend fun getCountryByAlpha3(alpha3: String, context: Context): Country? {
        if (countries == null) {
            countries =
                jsonParser.parseCountries(jsonReader.readJsonFromRaw(context, R.raw.countries_info))
        }
        return countries?.find { it.alpha3 == alpha3 }
    }


    suspend fun getAllCountries(context: Context): List<Country> {
        if (countries == null) {
            countries =
                jsonParser.parseCountries(jsonReader.readJsonFromRaw(context, R.raw.countries_info))
        }
        return countries!!
    }

    suspend fun getCountryByFullName(countryFullName: String, context: Context): Country? {
        if (countries == null) {
            countries =
                jsonParser.parseCountries(jsonReader.readJsonFromRaw(context, R.raw.countries_info))
        }
        return countries?.find { it.name == countryFullName }

    }
}
