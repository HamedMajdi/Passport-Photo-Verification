package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.UserRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.DatabaseService
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserBACVeiwModel(application: Application): AndroidViewModel(application) {

    private val coroutineScope: CoroutineScope
    private val userRepository: UserRepository

    private var _allUsers = MutableLiveData<List<UserBAC>>()
    val allUsers: LiveData<List<UserBAC>>
        get() = _allUsers

    private var _user = MutableLiveData<UserBAC>()

    val user: LiveData<UserBAC>
        get() = _user

    init {
        val userBACDao = DatabaseService.getInstance(application).userBACDao()
        coroutineScope = CoroutineScope(Dispatchers.IO)
        userRepository = UserRepository(userBACDao)
    }


    fun getUsers(){
        coroutineScope.launch {
            _allUsers.postValue(userRepository.getAllUsers())
        }
    }

    fun getUser(id: Int){
        coroutineScope.launch {
            _user.postValue(userRepository.getUser(id))
        }
    }
    fun addUser(userBAC: UserBAC){
        coroutineScope.launch {
            userRepository.insertUser(userBAC)
        }
    }

    fun updateUser(userBAC: UserBAC){
        coroutineScope.launch {
            userRepository.updateUser(userBAC)
        }
    }


    fun deleteUser(userBAC: UserBAC) {
        coroutineScope.launch {
            userRepository.deleteUser(userBAC)
            val updatedUsers = userRepository.getAllUsers()
            withContext(Dispatchers.Main) {
                _allUsers.value = updatedUsers
            }
        }
    }
}
