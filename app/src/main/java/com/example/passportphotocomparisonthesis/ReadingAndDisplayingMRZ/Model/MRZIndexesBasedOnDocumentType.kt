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
            docCheckDigitIndex = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE1

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE1
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE1
            birthDateCheckDigitIndex = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE1

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE1
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE1
            expirationDateCheckDigitIndex = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE1

            genderIndex = CONSTANTS.GENDER_INDEX_DOC_TYPE1
            documentTypeIndex = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE1

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE1
            nameSurNameEndIndex = CONSTANTS.NAME_SUBSTRING_END_INDEX_DOC_TYPE1

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
            docCheckDigitIndex = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE2

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE2
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE2
            birthDateCheckDigitIndex = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE2

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE2
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE2
            expirationDateCheckDigitIndex = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE2

            genderIndex = CONSTANTS.GENDER_INDEX_DOC_TYPE2
            documentTypeIndex = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE2

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE2
            nameSurNameEndIndex = CONSTANTS.NAME_SUBSTRING_END_INDEX_DOC_TYPE2

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
            docCheckDigitIndex = CONSTANTS.DOC_NUMBER_CHECK_DIGIT_INDEX_DOC_TYPE3

            birthDateStartIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_START_INDEX_DOC_TYPE3
            birthDateEndIndex = CONSTANTS.BIRTH_DATE_SUBSTRING_END_INDEX_DOC_TYPE3
            birthDateCheckDigitIndex = CONSTANTS.BIRTH_DATE_CHECK_DIGIT_INDEX_DOC_TYPE3

            expirationDateStartIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_START_INDEX_DOC_TYPE3
            expirationDateEndIndex = CONSTANTS.EXPIRATION_DATE_SUBSTRING_END_INDEX_DOC_TYPE3
            expirationDateCheckDigitIndex = CONSTANTS.EXPIRATION_DATE_CHECK_DIGIT_INDEX_DOC_TYPE3

            genderIndex = CONSTANTS.GENDER_INDEX_DOC_TYPE3
            documentTypeIndex = CONSTANTS.DOCUMENT_TYPE_DOC_TYPE3

            nameSurNameStartIndex = CONSTANTS.NAME_SUBSTRING_START_INDEX_DOC_TYPE3
            nameSurNameEndIndex = CONSTANTS.NAME_SUBSTRING_END_INDEX_DOC_TYPE3

            nationalityStartIndex = CONSTANTS.NATIONALITY_SUBSTRING_START_INDEX_DOC_TYPE3
            nationalityEndIndex = CONSTANTS.NATIONALITY_SUBSTRING_END_INDEX_DOC_TYPE3
        }
    }
}

class MRZIndexesBasedOnDocumentType(docType: Int) {

    var docNumberStartIndex = -1
    var docNumberEndIndex = -1
    var docCheckDigitIndex = -1

    var birthDateStartIndex = -1
    var birthDateEndIndex = -1
    var birthDateCheckDigitIndex = -1

    var expirationDateStartIndex = -1
    var expirationDateEndIndex = -1
    var expirationDateCheckDigitIndex = -1

    var genderIndex = -1
    var documentTypeIndex = -1

    var nameSurNameStartIndex = -1
    var nameSurNameEndIndex = -1

    var nationalityStartIndex = -1
    var nationalityEndIndex = -1

    init {
        when (docType){
            1 -> DocType1().setValues(this)
            2 -> DocType2().setValues(this)
            3 -> DocType3().setValues(this)
        }
    }
}
