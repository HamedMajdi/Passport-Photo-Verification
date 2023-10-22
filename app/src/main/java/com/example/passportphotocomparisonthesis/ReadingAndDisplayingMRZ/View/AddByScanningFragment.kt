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
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.example.passportphotocomparisonthesis.Utils.Camera.CameraHandler
import com.example.passportphotocomparisonthesis.Utils.Camera.ImageAnalyzer
import com.example.passportphotocomparisonthesis.Utils.Permissions.CameraPermissionRequest
import com.example.passportphotocomparisonthesis.Utils.Permissions.PermissionRequester
import com.example.passportphotocomparisonthesis.databinding.FragmentAddByScanningBinding



@ExperimentalGetImage class AddByScanningFragment : Fragment() {

    private lateinit var binding: FragmentAddByScanningBinding
    private lateinit var previewView: PreviewView
    private lateinit var rectView: RectangleView
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddByScanningBinding.inflate(layoutInflater, container, false)
        previewView = binding.camera
        rectView = binding.rectView

        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraHandler = CameraHandler(this, requireContext())
//        imageAnalyzer = ImageAnalyzer(viewModel, RectangleView(requireContext()))
        imageAnalyzer = ImageAnalyzer(viewModel, rectView)

        cameraPermissionRequester = CameraPermissionRequest(this, cameraRequestPermissionLauncher)

        checkAndRequestCameraAccess()

        viewModel.detectedDocumentNumber.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
//        viewModel.textDetected.observe(viewLifecycleOwner, Observer { text ->
//            if (text == "YOUR_PATTERN") {
////                findNavController().navigate(R.id.action_cameraFragment_to_newFragment)
//            }
//        })
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

    override fun onDestroy() {
        super.onDestroy()
        imageAnalyzer.stop()
    }
}
