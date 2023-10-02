package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import androidx.room.Entity

@Entity
data class UserBAC(
    val documentID: String,
    val expirationDate: String,
    val birthDate: String,
    val gender: String,
    val nationality: String,
    val travelDocumentType: Int
)
