package com.example.passportphotocomparisonthesis.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw.NFCVerificationFragment
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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