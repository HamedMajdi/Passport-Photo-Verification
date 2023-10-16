package com.example.passportphotocomparisonthesis.Utils.MRZ

import java.util.regex.Matcher

class PatternMatcher() {


    private val patternTD1 = PatternGenerator.getPatternTD1()
    private val patternTD2 = PatternGenerator.getPatternTD2()
    private val patternTD3 = PatternGenerator.getPatternTD3()

    lateinit var text: String
    lateinit var matcherTD1: Matcher
    lateinit var matcherTD2: Matcher
    lateinit var matcherTD3: Matcher
    fun setNewText(text: String){
        this.text = text
        matcherTD1 = patternTD1.matcher(text)
        matcherTD2 = patternTD2.matcher(text)
        matcherTD3 = patternTD3.matcher(text)

    }
    fun doesMatchWithMRZPattern() = matcherTD1.find() || matcherTD2.find() || matcherTD3.find()

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