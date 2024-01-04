package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.Country
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import kotlinx.coroutines.launch

//TODO: Delete this class
class CountryViewModel() : ViewModel() {

    private val _country = MutableLiveData<Country>()
    val country: LiveData<Country> get() = _country

    fun queryCountry(alpha3: String, context: Context) {
        val countryRepository = CountryRepository(JsonReader(), JsonParser())
        viewModelScope.launch {
            _country.value = countryRepository.getCountryByAlpha3(alpha3, context)
        }
    }

}