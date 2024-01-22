package com.example.passportphotocomparisonthesis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passportphotocomparisonthesis.databinding.FragmentOnboarding3Binding


class FragmentOnboarding3 : Fragment() {

    private lateinit var binding: FragmentOnboarding3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_queryFragmentNavHostContainer)
        }
    }


}