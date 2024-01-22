package com.example.passportphotocomparisonthesis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.databinding.FragmentOnboarding1Binding

class FragmentOnboarding1 : Fragment() {

    private lateinit var binding: FragmentOnboarding1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentOnboarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

}