package com.example.passportphotocomparisonthesis

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val screens = listOf(
        FragmentOnboarding1(),
        FragmentOnboarding2(),
        FragmentOnboarding3(),
    )

    override fun getItemCount() = screens.size

    override fun createFragment(position: Int) = screens[position]
}