package com.example.passportphotocomparisonthesis.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.R

/**
 * A simple [Fragment] subclass.
 * Use the [ModelTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModelTestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_model_test, container, false)
    }

}