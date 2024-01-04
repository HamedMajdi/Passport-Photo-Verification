package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data

import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBACDao

class UserRepository(private val userBACDao: UserBACDao) {

    suspend fun getAllUsers(): List<UserBAC> = userBACDao.getAllUsers()

    suspend fun getUser(id: Int): UserBAC? = userBACDao.getUser(id)

    suspend fun insertUser(userBAC: UserBAC) = userBACDao.insert(userBAC)

    suspend fun deleteUser(userBAC: UserBAC) = userBACDao.deleteUser(userBAC)
    suspend fun updateUser(userBAC: UserBAC) = userBACDao.updateUser(userBAC)
    suspend fun updateUserByID(
        documentId: String,
        name: String,
        gender: String,
        nationalityFull: String,
        nationalityFirst2: String,
        documentType: Int,
        userPhoto: ByteArray,
        userPhotoMimeType: String,
        isNFCVerified: Boolean) = userBACDao.updateUserByID(documentId, name, gender, nationalityFull, nationalityFirst2, documentType, userPhoto, userPhotoMimeType, isNFCVerified
    )


}