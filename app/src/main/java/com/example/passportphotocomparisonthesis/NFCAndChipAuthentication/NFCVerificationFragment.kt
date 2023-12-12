package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.databinding.FragmentNFCVerificationBinding

class NFCVerificationFragment : DialogFragment() {

    private lateinit var binding: FragmentNFCVerificationBinding
    private val args: NFCVerificationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNFCVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.test.setText("Document ID: ${args.documentID} \n Birth Date: ${args.birthDate} \n" +
                " Expiration Date: ${args.expirationDate} \n")
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt() // 85% of screen width
            val height =
                (resources.displayMetrics.heightPixels * 0.85).toInt() // 85% of screen height
            dialog.window?.setLayout(width, height)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}