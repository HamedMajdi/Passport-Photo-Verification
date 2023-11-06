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
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.example.passportphotocomparisonthesis.Utils.Camera.CameraHandler
import com.example.passportphotocomparisonthesis.Utils.Camera.ImageAnalyzer
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.Utils.Permissions.CameraPermissionRequest
import com.example.passportphotocomparisonthesis.Utils.Permissions.PermissionRequester
import com.example.passportphotocomparisonthesis.databinding.FragmentAddByScanningBinding



@ExperimentalGetImage class AddByScanningFragment : Fragment() {

    private lateinit var binding: FragmentAddByScanningBinding
    private lateinit var previewView: PreviewView
    private lateinit var viewModel: CameraViewModel
    private lateinit var cameraHandler: CameraHandler
    private lateinit var imageAnalyzer: ImageAnalyzer
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


        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraHandler = CameraHandler(this, requireContext())
        imageAnalyzer = ImageAnalyzer(viewModel)

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
        cameraHandler.setImageAnalyzer(imageAnalyzer)
    }

    private fun loadAndObserveViewModelVariables(){
        viewModel.detectedDocumentNumber.observe(viewLifecycleOwner, Observer {
            if (it!=null)
                binding.tvDocumentID.text = it
        })

        viewModel.detectedBirthDate.observe(viewLifecycleOwner, Observer {
            if (it!=null)
                binding.tvBirthDate.text = DateParser.parseDateFromRawToSlashFormat(it)
        })

        viewModel.detectedExpirationDate.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.tvExpirationDate.text = DateParser.parseDateFromRawToSlashFormat(it)
            }
        })

        viewModel.hasReceivedAll.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController()?.navigate(R.id.action_addDocumentFragment_to_userMRZFragment)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        imageAnalyzer.stop()
    }


}
