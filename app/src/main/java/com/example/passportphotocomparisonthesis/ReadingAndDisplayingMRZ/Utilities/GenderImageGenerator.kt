package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Utilities

import com.example.passportphotocomparisonthesis.R

enum class GenderImageGenerator(val id: String, val imageResId: Int) {
    MALE("M", R.drawable.ic_male),
    FEMALE("F", R.drawable.ic_female);

    companion object {
        fun fromId(id: String): GenderImageGenerator? {
            return values().firstOrNull { it.id == id }
        }
    }
}