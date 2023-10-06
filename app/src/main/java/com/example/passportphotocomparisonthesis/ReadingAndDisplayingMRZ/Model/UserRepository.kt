package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

class UserRepository(private val userBACDao: UserBACDao) {

    suspend fun getAllUsers(): List<UserBAC> = userBACDao.getAllUsers()

    suspend fun getUser(id: String): UserBAC? = userBACDao.getUser(id)

    suspend fun insertUser(userBAC: UserBAC) = userBACDao.insert(userBAC)

    suspend fun deleteUser(userBAC: UserBAC) = userBACDao.deleteUser(userBAC)
}