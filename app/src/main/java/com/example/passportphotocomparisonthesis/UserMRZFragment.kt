package com.example.passportphotocomparisonthesis

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View.AddDocumentFragmentDirections
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.SpinnerUtils
import com.example.passportphotocomparisonthesis.Utils.hideKeyboard
import com.example.passportphotocomparisonthesis.Utils.showDatePickerDialog
import com.example.passportphotocomparisonthesis.databinding.FragmentUserMRZBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import java.util.Calendar

class UserMRZFragment : Fragment() {

    private lateinit var binding: FragmentUserMRZBinding
    private val args: UserMRZFragmentArgs by navArgs()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var userViewModel: UserBACVeiwModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserMRZBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialData()
        toggleViewsEditibilityBasedOnVerifiedState(args.user)

        view.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
        }

        binding.editTextBirthDate.setOnClickListener {
            if (!checkIfUserIsNFCVerified(args.user)) {
                showDatePickerDialog(requireContext()) {
                    binding.editTextBirthDate.setText(it)
                }
            }

        }
        binding.editTextExpirationDate.setOnClickListener {
            if (!checkIfUserIsNFCVerified(args.user)) {
                showDatePickerDialog(requireContext()) {
                    binding.editTextExpirationDate.setText(it)
                }
            }
        }



        requireActivity().onBackPressedDispatcher.addCallback {
            if (binding.editTextDocumentNumber.text.isNullOrEmpty() || binding.editTextDocumentNumber.text!!.length != 9) {
                // Show Snackbar
                Snackbar.make(
                    binding.root,
                    "Please enter a valid document number.",
                    Snackbar.LENGTH_SHORT
                ).show()

                // Show AlertDialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Please enter a valid document number.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                // If validation passes, then call the super method to handle the back press
                isEnabled = false
                updateUser()
                requireActivity().onBackPressed()
            }

        }
    }

    private fun checkIfUserIsNFCVerified(user: UserBAC): Boolean = user.isNFCVerified

    private fun toggleViewsEditibilityBasedOnVerifiedState(user: UserBAC) {
        Log.d("DZ", "DZ ${checkIfUserIsNFCVerified(user)}")
        if (checkIfUserIsNFCVerified(user) == false) {
            binding.editTextDocumentNumber.isFocusable = true
            binding.editTextDocumentNumber.isFocusableInTouchMode = true

            binding.editTextBirthDate.isFocusable = true

            binding.editTextExpirationDate.isFocusable = true

            binding.editTextName.isFocusable = true
            binding.editTextName.isFocusableInTouchMode = true
        }
    }

    private fun initialData(){
        binding.editTextDocumentNumber.setText(args.user.documentID)
        binding.editTextBirthDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.birthDate))
        binding.editTextExpirationDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.expirationDate))
        binding.editTextName.setText(args.user.nameSurname)
    }

    private fun updateUser(){
        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)

        val userBAC = UserBAC(
            binding.editTextDocumentNumber.text.toString(),
            DateParser.parseDateFromSlashFormatToRaw(binding.editTextExpirationDate.text.toString())!!,
            DateParser.parseDateFromSlashFormatToRaw(binding.editTextBirthDate.text.toString())!!,
            binding.editTextName.text.toString(),
            args.user.gender,
            args.user.nationalityFullName,
            args.user.nationalityFirst2Digits,
            args.user.travelDocumentType
//            SpinnerUtils.getGender(binding.spinnerGender),
//            getCountryFullName(),
//            getCountryAlpha2Name(),
//            getDocumentType()
        )

        userViewModel.updateUser(userBAC)

    }
}

