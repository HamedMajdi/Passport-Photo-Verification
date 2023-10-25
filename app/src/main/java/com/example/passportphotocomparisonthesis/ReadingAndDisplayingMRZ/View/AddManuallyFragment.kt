package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.StaticDataRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import com.example.passportphotocomparisonthesis.databinding.FragmentAddManuallyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddManuallyFragment : Fragment() {

    private lateinit var binding: FragmentAddManuallyBinding
    private lateinit var coroutineScope: CoroutineScope
//    private lateinit var countriesDataForSpinner: List<SpinnerData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddManuallyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCountryTypeSpinner()
        loadDocumentTypeSpinner()
        loadGenderSpinner()

    }

    fun loadGenderSpinner() {
        val genderDataForSpinner = StaticDataRepository.getGenderOptions()
        val countryAdapter = SpinnerAdapter(requireContext(), genderDataForSpinner)
//        binding.spinnerGender.adapter = countryAdapter
//        binding.spinnerGender.adapter = countryAdapter

//        binding.spinnerGender.selectedItem as SpinnerData
    }

    fun loadDocumentTypeSpinner() {
        val documentDataForSpinner = StaticDataRepository.getDocumentTypeOptions()
        val countryAdapter = SpinnerAdapter(requireContext(), documentDataForSpinner)
        binding.spinnerDocumentType.adapter = countryAdapter
    }

    fun loadCountryTypeSpinner() {
        coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            val countriesDataForSpinner = StaticDataRepository.getCountryOptions(requireContext())
            val countryAdapter = SpinnerAdapter(requireContext(), countriesDataForSpinner)
            binding.spinnerCountry.adapter = countryAdapter
        }
    }

}