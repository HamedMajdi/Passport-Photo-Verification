package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Data.CountryRepository
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.Country
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.Utils.Camera.CameraHandler
import com.example.passportphotocomparisonthesis.Utils.Camera.MRZCodeImageAnalyzer
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonParser
import com.example.passportphotocomparisonthesis.Utils.JSON.JsonReader
import com.example.passportphotocomparisonthesis.Utils.Permissions.CameraPermissionRequest
import com.example.passportphotocomparisonthesis.Utils.Permissions.PermissionRequester
import com.example.passportphotocomparisonthesis.databinding.FragmentAddByScanningBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking


@ExperimentalGetImage class AddByScanningFragment : Fragment() {

    private lateinit var binding: FragmentAddByScanningBinding
    private lateinit var previewView: PreviewView
    private lateinit var viewModelCamera: CameraViewModel
    private lateinit var userViewModel: UserBACVeiwModel
    private lateinit var cameraHandler: CameraHandler
    private lateinit var mrzCodeImageAnalyzer: MRZCodeImageAnalyzer
    private lateinit var cameraPermissionRequester: PermissionRequester

    private val cameraRequestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(context,
                    "Camera Permission not Granted",
                    Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddByScanningBinding.inflate(layoutInflater, container, false)
        previewView = binding.camera


        viewModelCamera = ViewModelProvider(this).get(CameraViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraHandler = CameraHandler(this, requireContext())
        mrzCodeImageAnalyzer = MRZCodeImageAnalyzer(viewModelCamera)

        cameraPermissionRequester = CameraPermissionRequest(this, cameraRequestPermissionLauncher)

        checkAndRequestCameraAccess()

        loadAndObserveViewModelVariables()

    }
    fun checkAndRequestCameraAccess() {
        if (cameraPermissionRequester.isAccessGranted()) {
            startCamera()
        } else {
            cameraPermissionRequester.requestPermission()
        }
    }

    fun startCamera() {
        cameraHandler.startCamera(previewView.surfaceProvider, CameraSelector.DEFAULT_BACK_CAMERA)
        cameraHandler.setImageAnalyzer(mrzCodeImageAnalyzer)
    }

    private fun loadAndObserveViewModelVariables(){
        viewModelCamera.detectedDocumentNumber.observe(viewLifecycleOwner, Observer {
            if (it!=null)
                binding.tvDocumentID.text = it
        })

        viewModelCamera.detectedBirthDate.observe(viewLifecycleOwner, Observer {
            if (it!=null)
                binding.tvBirthDate.text = DateParser.parseDateFromRawToSlashFormat(it)
        })

        viewModelCamera.detectedExpirationDate.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.tvExpirationDate.text = DateParser.parseDateFromRawToSlashFormat(it)
            }
        })

        viewModelCamera.hasReceivedAll.observe(viewLifecycleOwner, Observer {
            if (it == true){
                val country = getCountryNameInfoByAlpha3(viewModelCamera.detectedNationality.value)

                val userBAC = UserBAC(
                    id = null,
                    viewModelCamera.detectedDocumentNumber.value!!,
                    viewModelCamera.detectedExpirationDate.value!!,
                    viewModelCamera.detectedBirthDate.value!!,
                    viewModelCamera.detectedName.value,
                    viewModelCamera.detectedGender.value,
                    country?.name,
                    country?.alpha2,
                    viewModelCamera.detectedDocumentType.value
                )

                userViewModel.addUser(userBAC)

                findNavController()?.navigate(R.id.action_addDocumentFragment_to_userMRZFragment)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mrzCodeImageAnalyzer.stop()
    }

    private fun getCountryNameInfoByAlpha3(alpha3: String?): Country?{
        val repository = CountryRepository(JsonReader(), JsonParser())
        var countryAlpha2: Country? = null

        runBlocking {
            countryAlpha2 = alpha3?.let { repository.getCountryByFullName(it, requireContext()) }
        }

        return countryAlpha2
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.selectedItemId = R.id.selectOrAddPassportFragment
    }


}
