package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import com.example.passportphotocomparisonthesis.Utils.CONSTANTS


interface DocType {
    fun setValues(mrzIndexes: MRZIndexesBasedOnDocumentType)
}

class DocType1 : DocType {

    override fun setValues(mrzIndexes: MRZIndexesBasedOnDocumentType) {
        mrzIndexes.apply {
            docNumberStartIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_START_INDEX_DOC_TYPE1
            docNumberEndIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_END_INDEX_DOC_TYPE1
            docCheckDigit = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE1

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE1
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE1
            birthDateCheckDigit = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE1

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE1
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE1
            expirationDateCheckDigit = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE1

            gender = CONSTANTS.GENDER_INDEX_DOC_TYPE1
            documentType = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE1

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE1

            nationalityStartIndex = CONSTANTS.NATIONALITY_SUBSTRING_START_INDEX_DOC_TYPE1
            nationalityEndIndex = CONSTANTS.NATIONALITY_SUBSTRING_END_INDEX_DOC_TYPE1
        }
    }
}

class DocType2 : DocType {
    override fun setValues(mrzIndexes: MRZIndexesBasedOnDocumentType) {
        mrzIndexes.apply {
            docNumberStartIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_START_INDEX_DOC_TYPE2
            docNumberEndIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_END_INDEX_DOC_TYPE2
            docCheckDigit = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE2

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE2
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE2
            birthDateCheckDigit = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE2

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE2
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE2
            expirationDateCheckDigit = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE2

            gender = CONSTANTS.GENDER_INDEX_DOC_TYPE2
            documentType = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE2

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE2

            nationalityStartIndex = CONSTANTS.NATIONALITY_SUBSTRING_START_INDEX_DOC_TYPE2
            nationalityEndIndex = CONSTANTS.NATIONALITY_SUBSTRING_END_INDEX_DOC_TYPE2
        }
    }
}

class DocType3 : DocType {
    override fun setValues(mrzIndexes: MRZIndexesBasedOnDocumentType) {
        mrzIndexes.apply {
            docNumberStartIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_START_INDEX_DOC_TYPE3
            docNumberEndIndex = CONSTANTS.DOC_NUMBER_SUBSTRING_END_INDEX_DOC_TYPE3
            docCheckDigit = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE3

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE3
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE3
            birthDateCheckDigit = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE3

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE3
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE3
            expirationDateCheckDigit = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE3

            gender = CONSTANTS.GENDER_INDEX_DOC_TYPE3
            documentType = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE3

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE3

            nationalityStartIndex = CONSTANTS.NATIONALITY_SUBSTRING_START_INDEX_DOC_TYPE3
            nationalityEndIndex = CONSTANTS.NATIONALITY_SUBSTRING_END_INDEX_DOC_TYPE3
        }
    }
}

class MRZIndexesBasedOnDocumentType(docType: Int) {

    var docNumberStartIndex= -1
    var docNumberEndIndex= -1
    var docCheckDigit= -1

    var birthDateStartIndex= -1
    var birthDateEndIndex= -1
    var birthDateCheckDigit= -1

    var expirationDateStartIndex= -1
    var expirationDateEndIndex= -1
    var expirationDateCheckDigit= -1

    var gender= -1
    var documentType= -1

    var nameSurNameStartIndex= -1

    var nationalityStartIndex= -1
    var nationalityEndIndex= -1

    init {
        when (docType){
            1 -> DocType1().setValues(this)
            2 -> DocType2().setValues(this)
            3 -> DocType3().setValues(this)
        }
    }
}
