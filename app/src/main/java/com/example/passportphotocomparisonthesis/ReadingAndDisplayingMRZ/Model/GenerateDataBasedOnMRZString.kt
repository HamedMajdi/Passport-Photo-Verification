package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

class GenerateDataBasedOnMRZString(private val rawText: String, private val documentType: Int) {

    val indexes = MRZIndexesBasedOnDocumentType(documentType)

    fun checkDocumentNumber(): Boolean{
        val calculatedCheckDigit = CheckGeneratedMRZValues.calculateDateCheckDigit(rawText)
        return calculatedCheckDigit == rawText.elementAt(indexes.docCheckDigitIndex)
    }
    fun getDocumentNumber(): String?{
        try {

        } catch (e: Exception){
            when(e) {
                is IndexOutOfBoundsException -> {
                    // handle IndexOutOfBoundsException
                }
//                is InvalidCheckDigitException -> {
//                    // handle InvalidCheckDigitException
//                }
//                is IncorrectFormatException -> {
//                    // handle IncorrectFormatException
//                }
                else -> throw e
            }
        }
        return rawText.substring(indexes.docNumberStartIndex, indexes.docNumberEndIndex)
    }

    fun getBirthDate(): String?{
        return rawText.substring(indexes.birthDateStartIndex, indexes.birthDateEndIndex)
    }

    fun getExpirationDate(): String?{
        return rawText.substring(indexes.expirationDateStartIndex, indexes.expirationDateEndIndex)
    }

    fun getGender(): String?{
        return rawText.substring(indexes.genderIndex)
    }
    fun getNationality(): String?{
        return rawText.substring(indexes.nationalityStartIndex, indexes.nationalityEndIndex)
    }

    fun getName(): String?{
        val splittedFirstNameAndLastName = rawText.split("<<")
        val lastName = splittedFirstNameAndLastName[0].replace("<", " ").trim()
        val firstName = splittedFirstNameAndLastName.drop(1).joinToString(" ") { it.replace("<", " ").trim() }
        return "$firstName  $lastName"
    }
}