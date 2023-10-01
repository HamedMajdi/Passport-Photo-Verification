package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import org.w3c.dom.DocumentType

data class UserBAC(
    val documentID: String,
    val expirationDate: String,
    val birthDate: String,
    val gender: String,
    val nationality: String,
    val travelDocumentType: Int
)
