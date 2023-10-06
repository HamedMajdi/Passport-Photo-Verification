package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_bac")
data class UserBAC(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "document_id")
    val documentID: String,

    @ColumnInfo(name = "expiration_date")
    val expirationDate: String,

    @ColumnInfo(name = "birth_date")
    val birthDate: String,

    @ColumnInfo(name = "name_surname")
    val nameSurname: String,

    val gender: String,

    @ColumnInfo(name = "nationality_full_name")
    val nationalityFullName: String,

    @ColumnInfo(name = "nationality_first_2_digits")
    val nationalityFirst2Digits: String,

    @ColumnInfo(name = "travel_document_type")
    val travelDocumentType: Int
)
