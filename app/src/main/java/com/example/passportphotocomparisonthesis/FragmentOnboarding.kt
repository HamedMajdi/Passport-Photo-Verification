package com.example.passportphotocomparisonthesis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.passportphotocomparisonthesis.databinding.FragmentOnboardingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentOnboarding : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        viewPager.adapter = OnboardingAdapter(childFragmentManager, lifecycle)
        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        return binding.root
    }


}