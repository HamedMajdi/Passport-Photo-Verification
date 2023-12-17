package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.SpinnerUtils
import com.example.passportphotocomparisonthesis.Utils.displayErrorMessageOnInputLayout
import com.example.passportphotocomparisonthesis.Utils.hideKeyboard
import com.example.passportphotocomparisonthesis.Utils.showDatePickerDialog
import com.example.passportphotocomparisonthesis.databinding.FragmentUserMRZBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

        view.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
        }

        initialData()
        toggleViewsEdibilityBasedOnVerifiedState(args.user)
        setOnClickListenerForViews()
//        handleBackButtonPress()
        clearErrorMessageIfDocumentIsValid()
        loadSpinners()

    }

    private fun checkIfUserIsNFCVerified(user: UserBAC): Boolean = user.isNFCVerified
    private fun toggleViewsEdibilityBasedOnVerifiedState(user: UserBAC) {
        if (checkIfUserIsNFCVerified(user) == false) {
            binding.editTextDocumentNumber.isFocusable = true
            binding.editTextDocumentNumber.isFocusableInTouchMode = true

            binding.editTextBirthDate.isFocusable = true

            binding.editTextExpirationDate.isFocusable = true

            binding.editTextName.isFocusable = true
            binding.editTextName.isFocusableInTouchMode = true
        }
    }

    private fun initialData() {
        binding.editTextDocumentNumber.setText(args.user.documentID)
        binding.editTextBirthDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.birthDate))
        binding.editTextExpirationDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.expirationDate))
        binding.editTextName.setText(args.user.nameSurname)
    }

    private fun updateUser() {
        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)

        val userBAC = UserBAC(
            id = args.user.id,
            binding.editTextDocumentNumber.text.toString(),
            DateParser.parseDateFromSlashFormatToRaw(binding.editTextExpirationDate.text.toString())!!,
            DateParser.parseDateFromSlashFormatToRaw(binding.editTextBirthDate.text.toString())!!,
            binding.editTextName.text.toString(),
            SpinnerUtils.getGender(binding.spinnerGender),
            SpinnerUtils.getCountryFullName(binding.spinnerCountry),
            runBlocking {
                SpinnerUtils.getCountryAlpha2Name(requireContext(), binding.spinnerCountry)
            },
            SpinnerUtils.getDocumentType(binding.spinnerDocumentType)
        )

        userViewModel.updateUser(userBAC)

    }

    private fun setOnClickListenerForViews() {

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

        binding.imageViewStartVerification.setOnClickListener {
            val action = UserMRZFragmentDirections.actionUserMRZFragmentToNFCVerificationFragment(
                binding.editTextDocumentNumber.text.toString(),
                binding.editTextBirthDate.text.toString(),
                binding.editTextExpirationDate.text.toString()
            )
            findNavController().navigate(action)
        }
    }

    private fun handleBackButtonPress() {
        requireActivity().onBackPressedDispatcher.addCallback {
        if (binding.editTextDocumentNumber.text.isNullOrEmpty() || binding.editTextDocumentNumber.text!!.length != 9) {

            displayErrorMessageOnInputLayout(binding.TILDocument, getString(R.string.doc_number_error), false)

        } else {
            // If validation passes, then call the super method to handle the back press
            isEnabled = false
            updateUser()
            requireActivity().onBackPressed()
        }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("OnDestroy", "onDestroy: 1")
        if (binding.editTextDocumentNumber.text.isNullOrEmpty() || binding.editTextDocumentNumber.text!!.length != 9) {
            Log.d("OnDestroy", "onDestroy: Inside if not available")

            displayErrorMessageOnInputLayout(binding.TILDocument, getString(R.string.doc_number_error), false)
        } else {
            Log.d("OnDestroy", "onDestroy: Inside update user")

            // If validation passes, then call the super method to handle the back press
            updateUser()
        }
    }


    private fun clearErrorMessageIfDocumentIsValid() {
        binding.editTextDocumentNumber.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrBlank() && text.length == 9)
                displayErrorMessageOnInputLayout(binding.TILDocument, null, true)
        }
    }


    private fun loadSpinners() {
        SpinnerUtils.loadGenderSpinner(binding.spinnerGender, requireContext())
        SpinnerUtils.loadDocumentTypeSpinner(binding.spinnerDocumentType, requireContext())

        SpinnerUtils.setGender(binding.spinnerGender, args.user.gender, requireContext())
        SpinnerUtils.setDocumentType(
            binding.spinnerDocumentType,
            args.user.travelDocumentType,
            requireContext()
        )


        coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            SpinnerUtils.loadCountrySpinner(binding.spinnerCountry, requireContext())
            SpinnerUtils.setCountry(
                binding.spinnerCountry,
                args.user.nationalityFullName,
                requireContext()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.selectOrAddPassportFragment
    }
}

