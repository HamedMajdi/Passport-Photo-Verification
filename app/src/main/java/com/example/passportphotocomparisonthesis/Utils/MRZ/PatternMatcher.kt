package com.example.passportphotocomparisonthesis.Utils.MRZ

import java.util.regex.Matcher

class PatternMatcher(private val text: String) {

    private val patternTD1 = PatternGenerator.getPatternTD1()
    private val patternTD2 = PatternGenerator.getPatternTD2()
    private val patternTD3 = PatternGenerator.getPatternTD3()

    val matcherTD1: Matcher = patternTD1.matcher(text)
    val matcherTD2: Matcher = patternTD2.matcher(text)
    val matcherTD3: Matcher = patternTD3.matcher(text)

    fun findDocumentType(): Int?{
        if (matcherTD1.find())
            return 1
        else if (matcherTD2.find())
            return 2
        else if (matcherTD3.find())
            return 3
        return null
    }

    fun textToParseBasedOnDocumentType(): String?{
        val docType = findDocumentType()
        if (docType != null){
            when(docType){
                1 -> return matcherTD1.group()
                2 -> return matcherTD2.group()
                3 -> return matcherTD3.group()
            }
        }
        return null
    }
}