package com.example.passportphotocomparisonthesis.UserSelfieAndFaceDetection.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.passportphotocomparisonthesis.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.util.concurrent.ExecutionException

class CameraXViewModel(application: Application) : AndroidViewModel(application) {

    private val cameraProviderLiveData: MutableLiveData<ProcessCameraProvider> = MutableLiveData()

    private val _detectionResult = MutableLiveData<String>()
    val detectionResult: LiveData<String> = _detectionResult

    private val _rectSelfie = MutableLiveData<Rect>()
    val rectSelfie: LiveData<Rect> = _rectSelfie

    private val _rectDocumentPhoto = MutableLiveData<Rect>()
    val rectDocumentPhoto: LiveData<Rect> = _rectDocumentPhoto

    private var detector: FaceDetector? = null

    private val context = getApplication<Application>().applicationContext
    fun processFaceDetectionOnSelfie(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()

        detector = FaceDetection.getClient(options)
        detector?.process(image)
            ?.addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    _detectionResult.value = context.getString(R.string.no_face_detected)
                } else
                    if (faces.size == 1) {
                        val face = faces[0]

                        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)

                        // Check if both eyes are detected
                        if (leftEye != null && rightEye != null) {
                            // Calculate the distance between the eyes and the difference in their heights
                            val eyeDistance = Math.abs(leftEye.position.x - rightEye.position.x)
                            val eyeHeightDifference =
                                Math.abs(leftEye.position.y - rightEye.position.y)

                            // Define the minimum distance between the eyes and the maximum difference in their heights
                            val MIN_EYE_DISTANCE = 10.0 // Decrease for less sensitivity
                            val MAX_EYE_HEIGHT_DIFFERENCE = 40.0 // Increase for less sensitivity

                            // Check if the face is not partially obscured or at an extreme angle
                            if (eyeDistance > MIN_EYE_DISTANCE && eyeHeightDifference < MAX_EYE_HEIGHT_DIFFERENCE) {

                                _rectSelfie.value = face.boundingBox

                            } else {

                                _detectionResult.value =
                                    context.getString(R.string.face_partially_observed)
                            }
                        } else {
                            _detectionResult.value =
                                context.getString(R.string.face_partially_observed)
                        }

                    } else if (faces.size > 1) {
                        _detectionResult.value =
                            context.getString(R.string.more_than_one_face_detected)

                    }
            }
            ?.addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun processFaceDetectionOnDocumentPhoto(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .build()

        detector = FaceDetection.getClient(options)
        detector?.process(image)
            ?.addOnSuccessListener { faces ->

                val face = faces[0]

                _rectDocumentPhoto.value = face.boundingBox
            }
            ?.addOnFailureListener { e ->
                // Handle failure
            }
    }

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
            cameraProviderFuture.addListener(
                {
                    try {
                        cameraProviderLiveData.setValue(cameraProviderFuture.get())
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                },
                ContextCompat.getMainExecutor(getApplication())
            )
            return cameraProviderLiveData
        }

    override fun onCleared() {
        super.onCleared()
        detector?.close()
    }

}