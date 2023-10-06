package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.DatabaseService
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBACDao
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserBACVeiwModel(application: Application): AndroidViewModel(application) {

    private val coroutineScope: CoroutineScope
    private val userRepository: UserRepository

    private var _users = MutableLiveData<List<UserBAC>>()
    val users: LiveData<List<UserBAC>>
        get() = _users


    init {
        val userBACDao = DatabaseService.getInstance(application).userBACDao()
        coroutineScope = CoroutineScope(Dispatchers.IO)
        userRepository = UserRepository(userBACDao)
    }

    fun getUsers(){
        coroutineScope.launch {
            _users.value = userRepository.getAllUsers()
        }
    }

    fun addUser(userBAC: UserBAC){
        coroutineScope.launch {
            userRepository.insertUser(userBAC)
        }
    }


}
