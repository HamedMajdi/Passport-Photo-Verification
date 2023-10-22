package com.example.passportphotocomparisonthesis.Utils.Camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import java.util.concurrent.Executors

/*
ProcessCameraProvider is a class in the CameraX library in Android. It’s a singleton class that
manages the camera lifecycle on behalf of the app, which means it binds the camera’s lifecycle to any
LifecycleOwner class. This eliminates the need for you to worry about opening and closing the camera.

The ProcessCameraProvider class is used to bind the camera’s lifecycle to your app’s lifecycle,
which means the camera only runs when your app is at the foreground,
improving your app’s performance and battery usage.

 */
class CameraHandler(private val lifecycleOwner: LifecycleOwner, private val context: Context) {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    fun startCamera(surfaceProvider: Preview.SurfaceProvider, selector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(surfaceProvider)
            }
            bindCamera(selector)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCamera(selector: CameraSelector) {


//        val analyzer = ImageAnalyzer(CameraViewModel())
//        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer)

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, selector,
                preview, imageAnalysis
            )
        } catch (exc: Exception) {
            // Handle any errors
        }
    }


    fun setImageAnalyzer(analyzer: ImageAnalysis.Analyzer) {
//        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        imageAnalysis = ImageAnalysis.Builder().build()
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer)
    }
}