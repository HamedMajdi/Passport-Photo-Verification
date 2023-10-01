package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model


data class UserBAC(
    val documentID: String,
    val expirationDate: String,
    val birthDate: String,
    val gender: String,
    val nationality: String,
    val travelDocumentType: Int
)
