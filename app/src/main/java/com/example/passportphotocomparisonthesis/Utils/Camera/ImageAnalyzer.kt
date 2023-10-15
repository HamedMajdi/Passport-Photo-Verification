package com.example.passportphotocomparisonthesis.Utils.Camera

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@ExperimentalGetImage class ImageAnalyzer(private val viewModel: CameraViewModel) : ImageAnalysis.Analyzer {

    private val analyzerScope = CoroutineScope(Dispatchers.Default)

    override fun analyze(imageProxy: ImageProxy) {
        analyzerScope.launch {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                viewModel.detectText(image)
            }
            imageProxy.close()
        }
    }

    fun stop() {
        analyzerScope.cancel()
    }
}