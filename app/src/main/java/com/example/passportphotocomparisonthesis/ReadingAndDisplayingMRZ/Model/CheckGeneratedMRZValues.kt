package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

class CheckGeneratedMRZValues {

    companion object{
        fun calculateDocumentNumberCheckDigit(documentNumber: String): Char {
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

        fun calculateDateCheckDigit(date: String): Char {
            val weights = intArrayOf(7, 3, 1, 7, 3, 1)
            var sum = 0

            for (i in date.indices) {
                val c = date[i]
                val value = if (c.isDigit()) {
                    c.toString().toInt()
                } else {
                    throw IllegalArgumentException("Invalid character in birth date: $c")
                }
                sum += value * weights[i % weights.size]
            }

            val checkDigit = sum % 10
            return Character.forDigit(checkDigit, 10)
        }

    }
}