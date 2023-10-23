package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CameraViewModel : ViewModel() {
    private val _detectedDocumentNumber = MutableLiveData<String>()
    val detectedDocumentNumber: LiveData<String> get() = _detectedDocumentNumber


    private val _detectedBirthDate = MutableLiveData<String>()
    val detectedBirthDate: LiveData<String> get() = _detectedBirthDate


    private val _detectedExpirationDate = MutableLiveData<String>()
    val detectedExpirationDate: LiveData<String> get() = _detectedExpirationDate



    private val _detectedName = MutableLiveData<String>()
    val detectedName: LiveData<String> get() = _detectedName



    private val _detectedNationality = MutableLiveData<String>()
    val detectedNationality: LiveData<String> get() = _detectedNationality



    private val _detectedGender = MutableLiveData<String>()
    val detectedGender: LiveData<String> get() = _detectedGender


    fun hasReceivedAll(): Boolean = detectedDocumentNumber != null && detectedExpirationDate != null && detectedBirthDate != null


    fun setDocumentNumber(documentNumber: String?){
        _detectedDocumentNumber.value = documentNumber ?:  _detectedDocumentNumber.value
    }
    fun setExpirationDate(expirationDate: String?){
        _detectedExpirationDate.value = expirationDate ?:  _detectedExpirationDate.value
    }
    fun setBirthDate(birthDate: String?){
        _detectedBirthDate.value = birthDate ?:  _detectedBirthDate.value
    }
    fun setName(name: String?){
        _detectedName.value = name ?:  _detectedName.value
    }
    fun setNationality(nationality: String?){
        _detectedNationality.value = nationality ?:  _detectedNationality.value
    }
    fun setGender(gender: String?){
        _detectedGender.value = gender ?:  _detectedGender.value
    }
}