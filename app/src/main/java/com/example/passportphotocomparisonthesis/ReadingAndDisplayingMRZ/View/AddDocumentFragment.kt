package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.databinding.FragmentAddDocumentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class AddDocumentFragment : Fragment() {

    private lateinit var binding: FragmentAddDocumentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout
        val fragmentScanning = AddByScanningFragment()
        val fragmentManual = AddManuallyFragment()

        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.scanning)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.manual)))

        // Set initial selected tab
        replaceFragment(fragmentScanning)
        setSelectedTabBackgroundColor(tabLayout.getTabAt(0))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setSelectedTabBackgroundColor(tab)
                when (tab?.position) {
                    0 -> replaceFragment(fragmentScanning)
                    1 -> replaceFragment(fragmentManual)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setDeselectedTabNull(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun setSelectedTabBackgroundColor(tab: TabLayout.Tab?) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected)
        val margin = 600.dpToPx()
        drawable?.setBounds(margin, margin, margin, margin)
        tab?.view?.background = drawable
    }

    private fun setDeselectedTabNull(tab: TabLayout.Tab?) {
        tab?.view?.background = null
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.selectedItemId = R.id.selectOrAddPassportFragment
    }

}