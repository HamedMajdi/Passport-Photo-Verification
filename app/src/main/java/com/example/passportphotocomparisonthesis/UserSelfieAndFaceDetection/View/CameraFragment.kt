package com.example.passportphotocomparisonthesis.UserSelfieAndFaceDetection.View

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.UserSelfieAndFaceDetection.ViewModel.CameraXViewModel
import com.example.passportphotocomparisonthesis.databinding.FragmentCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var viewModel: CameraXViewModel
    private var faceCount = 0
    private lateinit var imageCapture: ImageCapture
    private lateinit var detector: FaceDetector
    private lateinit var bitmapTakenPhoto: Bitmap
    private lateinit var bitmapDocumentPhoto: Bitmap
    private val args: CameraFragmentArgs by navArgs()

    private val animations = listOf(
        R.raw.photo_guide_mask,
        R.raw.photo_guide_glasses,
        R.raw.photo_guide_hat
    )
    val texts =
        listOf("Do NOT Wear a Mask", "You Must Take Your Glasses Off", "Do NOT Wear a Hat")

    var currentAnimationIndex = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalGetImage::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CameraXViewModel::class.java)

        binding.viewCamera.visibility = View.VISIBLE
        binding.viewResult.visibility = View.GONE


        bitmapTakenPhoto = args.documentPhoto
        viewModel.processFaceDetectionOnDocumentPhoto(bitmapTakenPhoto)


        cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()


        viewModel.processCameraProvider.observe(viewLifecycleOwner) { provider ->
            processCameraProvider = provider
            bindCameraPreview()
            bindInputAnalyser()
        }
        initiateAnimations()

        binding.capture.setOnClickListener {

            binding.viewCamera.visibility = View.GONE
            binding.viewResult.visibility = View.VISIBLE
            binding.animationDetectionStatus.playAnimation()

            val imageCapturedCallback = object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    bitmapTakenPhoto = imageProxyToBitmap(image)
                    viewModel.processFaceDetectionOnSelfie(bitmapTakenPhoto)

                    image.close()
                    // Stop the camera and face recognition
                    processCameraProvider.unbindAll()
                    if (::detector.isInitialized) {
                        detector.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                }
            }
            // Take a picture
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(requireContext()),
                imageCapturedCallback
            )
        }

        viewModel.rectSelfie.observe(viewLifecycleOwner, Observer { rect ->
            val croppedFace = Bitmap.createBitmap(
                bitmapTakenPhoto,
                rect.left,
                rect.top,
                rect.width(),
                rect.height()
            )

            animateResultAnimationView(R.raw.success_face_detection)
            binding.animationDetectionStatus.addAnimatorListener(object :
                Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}

                override fun onAnimationEnd(p0: Animator) {
                    val action =
                        CameraFragmentDirections.actionCameraFragmentToFaceComparisonFragment(
                            bitmapDocumentPhoto,
                            croppedFace
                        )
                    findNavController().navigate(action)
                }

                override fun onAnimationCancel(p0: Animator) {}

                override fun onAnimationRepeat(p0: Animator) {}
            })
        })

        viewModel.rectDocumentPhoto.observe(viewLifecycleOwner, Observer { rect ->
            bitmapDocumentPhoto = Bitmap.createBitmap(
                bitmapTakenPhoto,
                rect.left,
                rect.top,
                rect.width(),
                rect.height()
            )
        })

        viewModel.detectionResult.observe(viewLifecycleOwner, Observer { result ->
            animateResultAnimationView(R.raw.failed_face_detection)

            animateResultText(result)
        })
    }

    private fun initiateAnimations() {
        val animatorListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Fade out the animation
                val fadeOutAnimation =
                    ObjectAnimator.ofFloat(binding.animationView, "alpha", 1f, 0f)
                fadeOutAnimation.duration = 500
                fadeOutAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Load the next animation
                        currentAnimationIndex++

                        binding.animationView.setAnimation(animations[currentAnimationIndex % animations.size])

                        // Fade in the animation
                        val fadeInAnimation =
                            ObjectAnimator.ofFloat(binding.animationView, "alpha", 0f, 1f)
                        fadeInAnimation.duration = 500
                        fadeInAnimation.start()

                        binding.animationView.playAnimation()

                    }
                })
                fadeOutAnimation.start()

                // Fade out the text
                val fadeOutText = ObjectAnimator.ofFloat(binding.cameraTipsTv, "alpha", 1f, 0f)
                fadeOutText.duration = 500
                fadeOutText.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Change the text
                        binding.cameraTipsTv.text = texts[currentAnimationIndex % texts.size]

                        // Fade in the text
                        val fadeInText =
                            ObjectAnimator.ofFloat(binding.cameraTipsTv, "alpha", 0f, 1f)
                        fadeInText.duration = 500
                        fadeInText.start()
                    }
                })
                fadeOutText.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        }

        binding.animationView.addAnimatorListener(animatorListener)

        // Start the first animation
        binding.animationView.setAnimation(animations[currentAnimationIndex])
        binding.animationView.playAnimation()

        // Set the initial text
        binding.cameraTipsTv.text = texts[currentAnimationIndex]
    }

    private fun animateResultAnimationView(newAnimation: Int) {
        // Fade out the animation
        val fadeOutAnimation =
            ObjectAnimator.ofFloat(binding.animationDetectionStatus, "alpha", 1f, 0f)
        fadeOutAnimation.duration = 500
        fadeOutAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Change the animation
                binding.animationDetectionStatus.setAnimation(newAnimation)

                // Fade in the animation
                val fadeInAnimation = ObjectAnimator.ofFloat(binding.animationDetectionStatus, "alpha", 0f, 1f)
                fadeInAnimation.duration = 500
                fadeInAnimation.start()

                binding.animationDetectionStatus.playAnimation()
            }
        })
        fadeOutAnimation.start()
    }

    private fun animateResultText(newText: String) {

        // Fade out the text
        val fadeOutText = ObjectAnimator.ofFloat(binding.resultTV, "alpha", 1f, 0f)
        fadeOutText.duration = 500
        fadeOutText.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Change the text
                binding.resultTV.text = newText

                // Fade in the text
                val fadeInText = ObjectAnimator.ofFloat(binding.resultTV, "alpha", 0f, 1f)
                fadeInText.duration = 500
                fadeInText.start()
            }
        })
        fadeOutText.start()
    }


    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        // Correct the mirror effect
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Correct the rotation
        val rotationDegrees = when (image.imageInfo.rotationDegrees) {
            0 -> 0
            90 -> 90
            180 -> 180
            270 -> 270
            else -> throw IllegalArgumentException("Invalid rotation degrees: ${image.imageInfo.rotationDegrees}")
        }
        matrix.postRotate(rotationDegrees.toFloat())
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Flip the bitmap horizontally
        matrix.setScale(-1f, 1f, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        return bitmap
    }


    // Modify your bindCameraPreview function
    private fun bindCameraPreview() {
        cameraPreview = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(binding.previewView.surfaceProvider)

        // Initialize the ImageCapture instance here
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        try {
            // Bind the ImageCapture instance along with the Preview instance
            processCameraProvider.unbindAll() // Unbind all before binding
            processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview, imageCapture)
        } catch (illegalStateException: IllegalStateException) {
            Log.e("ERROR", illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e("ERROR", illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindInputAnalyser() {
        detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                .build()
        )
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val cameraExecutor = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(detector, imageProxy)
        }

        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
        } catch (illegalStateException: IllegalStateException) {
            Log.e("ERROR", illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e("ERROR", illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceDetector, imageProxy: ImageProxy) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        detector.process(inputImage).addOnSuccessListener { faces ->
            binding.faceBoundingBoxView.clear()
            faces.forEach { face ->
                val faceBox =
                    FaceBox(binding.faceBoundingBoxView, face, imageProxy.image!!.cropRect)
                binding.faceBoundingBoxView.add(faceBox)
            }
            faceCount = faces.size
            binding.capture.isEnabled = (faceCount == 1)


        }.addOnFailureListener {
            it.printStackTrace()
        }.addOnCompleteListener {
            imageProxy.close()
        }
    }

}