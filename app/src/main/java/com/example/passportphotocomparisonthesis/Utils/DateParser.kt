package com.example.passportphotocomparisonthesis.Utils

import java.text.SimpleDateFormat
import java.util.Locale
class DateParser {
    companion object{
        fun parseDate(date: String): String?{
            val inputFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            return try {
                val parsedDate = inputFormat.parse(date)
                val formattedDate = outputFormat.format(parsedDate)
                formattedDate
            } catch (e: Error){
                null
            }

        }
    }


}