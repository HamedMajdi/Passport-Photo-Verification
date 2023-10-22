package com.example.passportphotocomparisonthesis.Utils.Camera

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.GenerateDataBasedOnMRZString
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View.RectangleView
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.example.passportphotocomparisonthesis.Utils.MRZ.PatternMatcher
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ImageAnalyzer(private val viewModel: CameraViewModel, private val rectangleView: RectangleView) : ImageAnalysis.Analyzer {

    private val TAG_TRUE = "INSIDE TRUE"
    private val TAG_TRUE1 = "INSIDE TRUE1"

    private val analyzerScope = CoroutineScope(Dispatchers.Default)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        analyzerScope.launch {
            val mediaImage = imageProxy.image

            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->

                        val matcher = PatternMatcher()
                        for (block in visionText.textBlocks) {


//                            Log.d(TAG_TRUE1, "inside shit")


                            val blockText = block.text
                            val blockCornerPoints = block.cornerPoints
                            val blockFrame = block.boundingBox

                            rectangleView.blockFrame = blockFrame


                            matcher.setNewText(blockText)
                            if  (matcher.doesMatchWithMRZPattern()){

                                // Update the RectangleView with the new block frame
//                                rectangleView.blockFrame = blockFrame
//                                Log.d(TAG_TRUE, "$blockFrame")
//                                Log.d(TAG_TRUE, "${blockFrame!!.left}, ${blockFrame!!.top}, ${blockFrame!!.right}, ${blockFrame!!.bottom}")
//                                rectangleView.invalidate()  // Force the view to redraw


                                Log.d(TAG_TRUE, "inside true")
                                Log.d(TAG_TRUE, blockText)

                                val documentType = matcher.findDocumentType()
                                Log.d(TAG_TRUE, documentType.toString())
                                val stringToRecognize = matcher.textToParseBasedOnDocumentType()
                                val stringGenerator = GenerateDataBasedOnMRZString(stringToRecognize!!, documentType!!)
                                setNonNullDataToViewModel(stringGenerator)
                            }






                            for (line in block.lines) {
                                val lineText = line.text
//                                matcher.setNewText(lineText)
//                                if  (matcher.doesMatchWithMRZPattern()){
//                                    Log.d(TAG_TRUE, "inside true Line")
//                                    Log.d(TAG_TRUE, lineText)
//
//                                    val documentType = matcher.findDocumentType()
//                                    Log.d(TAG_TRUE, documentType.toString())
//                                    val stringToRecognize = matcher.textToParseBasedOnDocumentType()
//                                    val stringGenerator = GenerateDataBasedOnMRZString(stringToRecognize!!, documentType!!)
//                                    setNonNullDataToViewModel(stringGenerator)
//                                }


                                val lineCornerPoints = line.cornerPoints
                                val lineFrame = line.boundingBox

                                rectangleView.blockFrame = lineFrame
//                                Log.d(TAG_TRUE, "$blockFrame")
//                                Log.d(TAG_TRUE, "${blockFrame!!.left}, ${blockFrame!!.top}, ${blockFrame!!.right}, ${blockFrame!!.bottom}")
                                rectangleView.invalidate()  // Force the view to redraw

                            }
                        }

                    }
                    .addOnFailureListener { e ->
                        // Handle any errors
                    }
                    .addOnCompleteListener {
                        // Close the image,this tells CameraX to feed the next image to the analyzer
                        imageProxy.close()
                    }
            }
        }
    }

    fun stop() {
        analyzerScope.cancel()
    }



    private fun setNonNullDataToViewModel(dataGenerator: GenerateDataBasedOnMRZString) {
        viewModel.setDocumentNumber(dataGenerator.getDocumentNumber())
        viewModel.setBirthDate(dataGenerator.getBirthDate())
        viewModel.setExpirationDate(dataGenerator.getExpirationDate())
        viewModel.setName(dataGenerator.getName())
        viewModel.setNationality(dataGenerator.getNationality())
        viewModel.setGender(dataGenerator.getGender())
    }
}