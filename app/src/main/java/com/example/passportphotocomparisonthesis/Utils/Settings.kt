package com.example.passportphotocomparisonthesis.Utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class Settings {
    companion object{
        fun setLocale(lang: String, resources: android.content.res.Resources) {
            val myLocale = Locale(lang)
            Locale.setDefault(myLocale)
            val config = Configuration()
            config.locale = myLocale
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        fun applyDarkMode(mode: Int) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }

    }
}