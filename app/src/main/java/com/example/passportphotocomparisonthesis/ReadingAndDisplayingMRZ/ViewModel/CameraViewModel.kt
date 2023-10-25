package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CameraViewModel : ViewModel() {
    private val _detectedDocumentNumber = MutableLiveData<String>(null)
    val detectedDocumentNumber: LiveData<String> get() = _detectedDocumentNumber


    private val _detectedBirthDate = MutableLiveData<String>(null)
    val detectedBirthDate: LiveData<String> get() = _detectedBirthDate


    private val _detectedExpirationDate = MutableLiveData<String>(null)
    val detectedExpirationDate: LiveData<String> get() = _detectedExpirationDate



    private val _detectedName = MutableLiveData<String>(null)
    val detectedName: LiveData<String> get() = _detectedName



    private val _detectedNationality = MutableLiveData<String>(null)
    val detectedNationality: LiveData<String> get() = _detectedNationality



    private val _detectedGender = MutableLiveData<String>(null)
    val detectedGender: LiveData<String> get() = _detectedGender

    private val _hasReceivedAll = MutableLiveData<Boolean>(false)
    val hasReceivedAll: LiveData<Boolean> get() = _hasReceivedAll



    private fun hasReceivedAll() {
        if (detectedDocumentNumber.value != null && detectedExpirationDate.value != null && detectedBirthDate.value != null){
            _hasReceivedAll.value = true
        }
    }


    fun setDocumentNumber(documentNumber: String?){

        if (_detectedDocumentNumber.value == null){
            _detectedDocumentNumber.value = documentNumber ?:  null
            hasReceivedAll()
        }
    }
    fun setExpirationDate(expirationDate: String?){
        if (_detectedExpirationDate.value == null){
            _detectedExpirationDate.value = expirationDate ?:  null
            hasReceivedAll()
        }
    }
    fun setBirthDate(birthDate: String?){
        if (_detectedBirthDate.value == null){
            _detectedBirthDate.value = birthDate ?:  null
            hasReceivedAll()
        }
    }
    fun setName(name: String?){
        if (_detectedName.value == null)
            _detectedName.value = name ?:  _detectedName.value
    }
    fun setNationality(nationality: String?){
        if (_detectedNationality.value == null)
            _detectedNationality.value = nationality ?:  _detectedNationality.value
    }
    fun setGender(gender: String?){
        if (_detectedGender.value == null)
            _detectedGender.value = gender ?:  _detectedGender.value
    }
}