package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Utilities

import com.example.passportphotocomparisonthesis.R

enum class DocTypeImageGenerator(val id: Int, val imageResId: Int) {
    TYPE1(1, R.drawable.ic_type1),
    TYPE2(2, R.drawable.ic_type2),
    TYPE3(3, R.drawable.ic_type3);

    companion object {
        fun fromId(id: Int): DocTypeImageGenerator? {
            return values().firstOrNull { it.id == id }
        }
    }
}