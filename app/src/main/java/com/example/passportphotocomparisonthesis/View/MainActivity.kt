package com.example.passportphotocomparisonthesis.View

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CountryViewModel
import com.example.passportphotocomparisonthesis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var  countryViewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        countryViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)

        countryViewModel.queryCountry("IRN", applicationContext)
        countryViewModel.country.observe(this, Observer {
            Log.d("SHIT", "${it.name}, ${it.alpha2}, ${it.alpha3}")
        })

        navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.selectOrAddPassportFragment,
                R.id.modelTestFragment,
                R.id.settingsFragment
            )
        )

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}