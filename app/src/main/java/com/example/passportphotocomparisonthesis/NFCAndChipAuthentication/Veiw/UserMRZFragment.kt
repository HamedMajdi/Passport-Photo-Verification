package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.Image.ImageUtil.decodeImage
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
import java.io.ByteArrayInputStream
import java.io.InputStream

class UserMRZFragment : Fragment() {

    private lateinit var binding: FragmentUserMRZBinding
    private val args: com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw.UserMRZFragmentArgs by navArgs()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var userViewModel: UserBACVeiwModel
    private lateinit var user: UserBAC


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

        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)

        view.setOnClickListener {
            hideKeyboard(requireContext(), requireActivity())
        }
        user = args.user


        initialData()
        toggleViewsEdibilityBasedOnVerifiedState(args.user)
        setOnClickListenerForViews()
        clearErrorMessageIfDocumentIsValid()
        loadSpinners(args.user.gender, args.user.travelDocumentType, args.user.nationalityFullName)
        setUserPhoto()
    }

    private fun checkIfUserIsNFCVerified(user: UserBAC): Boolean = user.isNFCVerified
    private fun toggleViewsEdibilityBasedOnVerifiedState(user: UserBAC) {
        if (checkIfUserIsNFCVerified(user) == false) {
            binding.viewVerified.visibility = View.GONE
            binding.viewNotVerified.visibility = View.VISIBLE
            binding.editTextDocumentNumber.isFocusable = true
            binding.editTextDocumentNumber.isFocusableInTouchMode = true

            binding.editTextBirthDate.isFocusable = true

            binding.editTextExpirationDate.isFocusable = true

            binding.editTextName.isFocusable = true
            binding.editTextName.isFocusableInTouchMode = true

        } else {
            binding.viewVerified.visibility = View.VISIBLE
            binding.viewNotVerified.visibility = View.GONE
            binding.spinnerGender.setOnTouchListener { v, event -> true }
            binding.spinnerDocumentType.setOnTouchListener { v, event -> true }
            binding.spinnerCountry.setOnTouchListener { v, event -> true }
        }
    }

    private fun setUserPhoto() {
        if (checkIfUserIsNFCVerified(user)) {

            val imageBuffer = user.userImage
            val inputStream: InputStream = ByteArrayInputStream(imageBuffer)
            val bitmap = decodeImage(requireContext(), user.imageMimeType!!, inputStream)
            inputStream.close()
            binding.imageViewUserPhotoDocument.setImageBitmap(bitmap)
        }
    }

    private fun initialData() {
        binding.editTextDocumentNumber.setText(args.user.documentID)
        binding.editTextBirthDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.birthDate))
        binding.editTextExpirationDate.setText(DateParser.parseDateFromRawToSlashFormat(args.user.expirationDate))
        binding.editTextName.setText(args.user.nameSurname)
    }

    private fun updateUser() {


        if (!checkIfUserIsNFCVerified(user)) {
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
            val action =
                com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw.UserMRZFragmentDirections.actionUserMRZFragmentToNFCVerificationFragment(
                    binding.editTextDocumentNumber.text.toString(),
                    binding.editTextBirthDate.text.toString(),
                    binding.editTextExpirationDate.text.toString()
                )
            findNavController().navigate(action)
        }

        binding.imageViewStartCamera.setOnClickListener {
            val imageBuffer = user.userImage
            val inputStream: InputStream = ByteArrayInputStream(imageBuffer)
            val bitmap = decodeImage(requireContext(), user.imageMimeType!!, inputStream)
            inputStream.close()

            val action = UserMRZFragmentDirections.actionUserMRZFragmentToCameraFragment(
                bitmap
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.editTextDocumentNumber.text.isNullOrEmpty() || binding.editTextDocumentNumber.text!!.length != 9) {

            displayErrorMessageOnInputLayout(
                binding.TILDocument,
                getString(R.string.doc_number_error),
                false
            )
        } else {
            updateUser()
        }
    }


    private fun clearErrorMessageIfDocumentIsValid() {
        binding.editTextDocumentNumber.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrBlank() && text.length == 9)
                displayErrorMessageOnInputLayout(binding.TILDocument, null, true)
        }
    }


    private fun loadSpinners(gender: String?, docType: Int?, country: String?) {
        SpinnerUtils.loadGenderSpinner(binding.spinnerGender, requireContext())
        SpinnerUtils.loadDocumentTypeSpinner(binding.spinnerDocumentType, requireContext())

        SpinnerUtils.setGender(binding.spinnerGender, gender, requireContext())
        SpinnerUtils.setDocumentType(
            binding.spinnerDocumentType,
            docType,
            requireContext()
        )


        coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            SpinnerUtils.loadCountrySpinner(binding.spinnerCountry, requireContext())
            activity?.let {
                SpinnerUtils.setCountry(
                    binding.spinnerCountry,
                    country,
                    it.applicationContext
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)
        args.user.id?.let { userViewModel.getUser(it) }
        userViewModel.user.observe(viewLifecycleOwner, Observer {
            user = it
            binding.editTextDocumentNumber.setText(it.documentID)
            binding.editTextBirthDate.setText(DateParser.parseDateFromRawToSlashFormat(it.birthDate))
            binding.editTextExpirationDate.setText(DateParser.parseDateFromRawToSlashFormat(it.expirationDate))
            binding.editTextName.setText(it.nameSurname)
            loadSpinners(it.gender, it.travelDocumentType, it.nationalityFullName)
            toggleViewsEdibilityBasedOnVerifiedState(user)

        })

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.selectOrAddPassportFragment
    }
}

