package com.example.passportphotocomparisonthesis.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.databinding.FragmentQueryNavHostContainerBinding


class QueryFragmentNavHostContainer : Fragment() {

    private lateinit var binding: FragmentQueryNavHostContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQueryNavHostContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

}