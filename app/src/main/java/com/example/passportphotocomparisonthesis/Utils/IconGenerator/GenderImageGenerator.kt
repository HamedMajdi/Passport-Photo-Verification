package com.example.passportphotocomparisonthesis.Utils.IconGenerator

import com.example.passportphotocomparisonthesis.R

enum class GenderImageGenerator(val id: String, val imageResId: Int) {
    MALE("M", R.drawable.ic_male),
    FEMALE("F", R.drawable.ic_female),
    NONBINARY("<", R.drawable.ic_non_binary);

    companion object {
        fun fromId(id: String): GenderImageGenerator? {
            return values().firstOrNull { it.id == id }
        }
    }

}