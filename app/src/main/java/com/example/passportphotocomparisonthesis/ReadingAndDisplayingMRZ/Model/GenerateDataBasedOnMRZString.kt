package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import com.example.passportphotocomparisonthesis.Utils.MRZ.PatternMatcher

class GenerateDataBasedOnMRZString(private val rawText: String, private val documentType: Int) {


    fun getIndexes() {
        val indexes = MRZIndexesBasedOnDocumentType(documentType)
    }

}