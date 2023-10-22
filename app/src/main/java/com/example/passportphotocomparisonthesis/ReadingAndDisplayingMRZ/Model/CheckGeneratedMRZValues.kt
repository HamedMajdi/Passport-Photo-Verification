package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import android.util.Log
import java.lang.IndexOutOfBoundsException

class CheckGeneratedMRZValues {

    companion object {
        fun isDocumentCheckDigitValid(documentNumber: String, checkDigit: Char): Boolean{
            val calculatedCheckDigit = calculateDocumentNumberCheckDigit(documentNumber)
            return checkDigit == calculatedCheckDigit
            return false
        }

        fun isDateCheckDigitValid(date: String, checkDigit: Char): Boolean{
            val calculatedCheckDigit = calculateDateCheckDigit(date)
            return checkDigit == calculatedCheckDigit
            return false
        }

        private fun calculateDocumentNumberCheckDigit(documentNumber: String): Char {
            val weights = intArrayOf(7, 3, 1, 7, 3, 1, 7, 3, 1)
            var sum = 0

            for (i in documentNumber.indices) {
                val c = documentNumber[i]
                val value = if (c.isDigit()) {
                    c.toString().toInt()
                } else {
                    c - 'A' + 10
                }
                sum += value * weights[i]
            }

            val checkDigit = sum % 10
            return Character.forDigit(checkDigit, 10)
        }

        private fun calculateDateCheckDigit(date: String): Char {
            val weights = intArrayOf(7, 3, 1, 7, 3, 1)
            var sum = 0

            for (i in date.indices) {
                val c = date[i]
                val value = if (c.isDigit()) {
                    c.toString().toInt()
                } else {
//                    throw IllegalArgumentException("Invalid character in birth date: $c")
                    Log.e("ERROR", "Invalid character in date: $c", )
                    return ' '
                }
                sum += value * weights[i % weights.size]
            }

            val checkDigit = sum % 10
            return Character.forDigit(checkDigit, 10)
        }


        fun isIndexingCorrect(data: String, startIndex: Int, endIndex: Int): Boolean {
            return try {
                data.substring(startIndex, endIndex)
                true
            } catch (e: IndexOutOfBoundsException){
                false
            }
        }

        /*
        This function uses a regular expression to match the pattern you described.
        The matches function checks if the entire input string matches the pattern.
        The ^ at the start of the pattern ensures that the match must start at the beginning of the string.
        The [a-zA-Z]+(<[a-zA-Z]+)* matches one or more letters, which can contain one or more ‘<’ which are not sequential, << matches exactly two ‘<’ characters,
        and [a-zA-Z]+(<[a-zA-Z]*)* matches some letters followed by zero or more ‘<’ characters,
        and $ at the end ensures that the match must end with some letters (which can contain one or more ‘<’ which are not sequential).
        If more than one ‘<’ exist, there should be no more letters after that.
         */
        fun isNamingAccordingToConvention(name: String, startIndex: Int, endIndex: Int): Boolean{
            val pattern = Regex("^[a-zA-Z]+(<[a-zA-Z]+)*<<[a-zA-Z]+(<[a-zA-Z]*)*$")
            return pattern.matches(name)
        }
    }
}