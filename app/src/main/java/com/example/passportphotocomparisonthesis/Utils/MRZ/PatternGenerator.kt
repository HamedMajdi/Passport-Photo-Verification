package com.example.passportphotocomparisonthesis.Utils.MRZ

import java.util.regex.Pattern

class PatternGenerator {
    companion object{

        private val patternTD1 = Pattern.compile("^[A-Z0-9<]{30}$")
        private val patternTD2 = Pattern.compile("^[A-Z0-9<]{36}$")
        private val patternTD3 = Pattern.compile("^[A-Z0-9<]{44}$")
        fun getPatternTD1(): Pattern{
            return patternTD1
        }
        fun getPatternTD2(): Pattern{
            return patternTD2
        }
        fun getPatternTD3(): Pattern{
            return patternTD3
        }
    }

}