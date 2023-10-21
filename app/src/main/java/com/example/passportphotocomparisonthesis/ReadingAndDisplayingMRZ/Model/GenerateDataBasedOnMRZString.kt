package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel

class GenerateDataBasedOnMRZString(private val rawText: String, private val documentType: Int) {

    val indexes = MRZIndexesBasedOnDocumentType(documentType)
    val cameraViewModel = CameraViewModel()

    fun getDocumentNumber(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(rawText, indexes.docNumberStartIndex, indexes.docNumberEndIndex)) {
            val extractedDocumentNumber =
                rawText.substring(indexes.docNumberStartIndex, indexes.docNumberEndIndex)
            val extractedDocumentNumberCheckDigit = rawText.elementAt(indexes.docCheckDigitIndex)

            if (CheckGeneratedMRZValues.isDocumentCheckDigitValid(extractedDocumentNumber, extractedDocumentNumberCheckDigit)) {
                return extractedDocumentNumber
            }
        }
        return null
    }

    fun getBirthDate(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(rawText, indexes.birthDateStartIndex, indexes.birthDateEndIndex)) {
            val extractedDate =
                rawText.substring(indexes.birthDateStartIndex, indexes.birthDateEndIndex)
            val extractedDateCheckDigit = rawText.elementAt(indexes.birthDateCheckDigitIndex)

            if (CheckGeneratedMRZValues.isDateCheckDigitValid(extractedDate, extractedDateCheckDigit)) {
                return extractedDate
            }
        }
        return null
    }

    fun getExpirationDate(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(rawText, indexes.expirationDateStartIndex, indexes.expirationDateEndIndex)) {
            val extractedDate =
                rawText.substring(indexes.expirationDateStartIndex, indexes.expirationDateEndIndex)
            val extractedDateCheckDigit = rawText.elementAt(indexes.expirationDateCheckDigitIndex)

            if (CheckGeneratedMRZValues.isDateCheckDigitValid(extractedDate, extractedDateCheckDigit)) {
                return extractedDate
            }
        }
        return null
    }

    fun getGender(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(
                rawText,
                indexes.genderIndex,
                indexes.genderIndex + 1
            )
        )
            return rawText.substring(indexes.genderIndex, indexes.genderIndex + 1)
        return null
    }

    fun getNationality(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(
                rawText,
                indexes.nationalityStartIndex,
                indexes.nationalityEndIndex
            )
        )
            return rawText.substring(indexes.genderIndex, indexes.genderIndex + 1)
        return null
    }

    fun getName(): String? {
        if (CheckGeneratedMRZValues.isIndexingCorrect(
                rawText,
                indexes.nameSurNameStartIndex,
                indexes.nameSurNameEndIndex
            )
        )
            if (CheckGeneratedMRZValues.isNamingAccordingToConvention(
                    rawText,
                    indexes.nameSurNameStartIndex,
                    indexes.nameSurNameEndIndex
                )
            )
                return extractName()
        return null
    }

    private fun extractName(): String? {
        val splittedFirstNameAndLastName = rawText.split("<<")
        val lastName = splittedFirstNameAndLastName[0].replace("<", " ").trim()
        val firstName =
            splittedFirstNameAndLastName.drop(1).joinToString(" ") { it.replace("<", " ").trim() }
        return "$firstName  $lastName"
    }
}