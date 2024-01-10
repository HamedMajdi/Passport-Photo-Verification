package com.example.passportphotocomparisonthesis.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw.NFCVerificationFragment
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.Utils.Settings
import com.example.passportphotocomparisonthesis.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Load the user's language preference
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultLanguage = Locale.getDefault().language
        val savedLanguage = sharedPref.getString(getString(R.string.saved_language_key), defaultLanguage)

        // Update the locale
        if (savedLanguage != null) {
            Settings.setLocale(savedLanguage, resources)
        }

        // Load the user's dark mode preference
        val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val savedMode = sharedPref.getInt(getString(R.string.saved_dark_mode_key), defaultMode)
        Settings.applyDarkMode(savedMode)







        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.selectOrAddPassportFragment,
                R.id.modelTestFragment,
                R.id.settingsFragment
            )
        )

        binding.bottomNavigationView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        Log.d("NFC", "onNewIntent: ${navHostFragment.childFragmentManager.fragments.size}")

        val currentFragment = navHostFragment.childFragmentManager.fragments[0]
        if (currentFragment is NFCVerificationFragment) {
            currentFragment.onNewIntent(intent, this)
        }
    }

}