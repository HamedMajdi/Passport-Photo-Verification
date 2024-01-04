package com.example.finalnfcpassport

import android.app.Application
import android.nfc.tech.IsoDep
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Data.MyRepository
import com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Data.PassportData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.UserRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.DatabaseService
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jmrtd.BACKeySpec
import org.jmrtd.lds.iso19794.FaceImageInfo
import java.io.DataInputStream

class PassportNFCViewModel(private val application: Application) : AndroidViewModel(application) {
    private val isLoading = MutableLiveData<Boolean>()
    val _isLoading: LiveData<Boolean> get() = isLoading

    private val isSuccessful = MutableLiveData<Boolean>()
    val _isSuccessful: LiveData<Boolean> get() = isSuccessful

    private val passportData = MutableLiveData<PassportData>()
    val _passportData: LiveData<PassportData> get() = passportData

    private val error = MutableLiveData<Exception>()
    val _error: LiveData<Exception> get() = error

    private lateinit var repository: MyRepository

    private val userRepository: UserRepository
    var retrivedFile: FaceImageInfo? = null
    var buffer: ByteArray? = null

    private val coroutineScope: CoroutineScope

    init {
        val userBACDao = DatabaseService.getInstance(application).userBACDao()
        userRepository = UserRepository(userBACDao)
        coroutineScope = CoroutineScope(Dispatchers.IO)

    }

    fun readPassportData(isoDep: IsoDep, bacKey: BACKeySpec) {

        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                repository = MyRepository()
                val data = repository.readPassportData(isoDep, bacKey, application)

                passportData.postValue(data)
                isLoading.postValue(false)
                isSuccessful.postValue(true)

            } catch (e: Exception) {

                Log.d("NFC", e.toString())
                isLoading.postValue(false)
                isSuccessful.postValue(false)
                error.postValue(e)
            }
        }
    }

    fun updateUserWithPassportData(data: PassportData) {
        coroutineScope.launch {

            Log.d("NFC", "updateUser")
            Log.d("NFC", "updateUser ${data.dg1File.mrzInfo.documentNumber}")
            val allFaceImageInfo: MutableList<FaceImageInfo> = ArrayList()

            data.dg2File.faceInfos.forEach {
                allFaceImageInfo.addAll(it.faceImageInfos)
            }


            if (allFaceImageInfo.isNotEmpty()) {
                retrivedFile = allFaceImageInfo.first()
                val dataInputStream = DataInputStream(retrivedFile!!.imageInputStream)
                buffer = ByteArray(retrivedFile!!.imageLength)
                dataInputStream.readFully(buffer, 0, retrivedFile!!.imageLength)
            }
            val countryRepository = CountryRepository(JsonReader(), JsonParser())

            try {
                userRepository.updateUserByID(
                    data.dg1File.mrzInfo.documentNumber,
                    data.dg1File.mrzInfo.secondaryIdentifier.replace(
                        "<",
                        ""
                    ) + " " + data.dg1File.mrzInfo.primaryIdentifier.replace("<", ""),
                    data.dg1File.mrzInfo.gender.toString().first().toString(),
                    countryRepository.getCountryByAlpha3(
                        data.dg1File.mrzInfo.nationality,
                        application
                    )!!.name,
                    countryRepository.getCountryByAlpha3(
                        data.dg1File.mrzInfo.nationality,
                        application
                    )!!.alpha2,
                    data.dg1File.mrzInfo.documentType,
                    buffer!!,
                    retrivedFile!!.mimeType,
                    true
                )

                Log.d("NFC", "Updated Succesfully")

            } catch (e: Exception) {
                Log.d("NFC", e.toString())
            }
        }
    }
}
