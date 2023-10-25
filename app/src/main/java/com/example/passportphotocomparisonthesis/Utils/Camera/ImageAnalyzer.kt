package com.example.passportphotocomparisonthesis.Utils.Camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.GenerateDataBasedOnMRZString
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.CameraViewModel
import com.example.passportphotocomparisonthesis.Utils.MRZ.PatternMatcher
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ImageAnalyzer(private val viewModel: CameraViewModel) : ImageAnalysis.Analyzer {


    private val TAG_TRUE = "INSIDE TRUE"
    private val TAG_BLOCK = "INSIDE BLOCK"
    private val TAG_LINE = "INSIDE LINE"

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


                            Log.d(TAG_TRUE, "analyze: ")


                            val blockText = block.text
                            val blockCornerPoints = block.cornerPoints
                            val blockFrame = block.boundingBox
//                            rectangleView.blockFrame = blockFrame
//                            Log.e(TAG_BLOCK, "$blockText")

//                            matcher.setNewText(blockText)
//                            if  (matcher.doesMatchWithMRZPattern()){

                                // Update the RectangleView with the new block frame
//                                rectangleView.blockFrame = blockFrame
//                                Log.d(TAG_TRUE, "$blockFrame")
//                                Log.d(TAG_TRUE, "${blockFrame!!.left}, ${blockFrame!!.top}, ${blockFrame!!.right}, ${blockFrame!!.bottom}")
//                                rectangleView.invalidate()  // Force the view to redraw


//                                Log.d(TAG_TRUE, "inside true")
//                                Log.d(TAG_TRUE, blockText)

//                                val documentType = matcher.findDocumentType()
//                                Log.d(TAG_TRUE, documentType.toString())
//                                val stringToRecognize = matcher.textToParseBasedOnDocumentType()
//                                val stringGenerator = GenerateDataBasedOnMRZString(stringToRecognize!!, documentType!!)
//
//                                Log.d(TAG_TRUE, "Document: ${stringGenerator.getDocumentNumber()}")
//                                Log.d(TAG_TRUE, "BIRTH: ${stringGenerator.getBirthDate()}")
//                                Log.d(TAG_TRUE, "Expiration: ${stringGenerator.getExpirationDate()}")
//                                setNonNullDataToViewModel(stringGenerator)
//                            }






                            for (line in block.lines) {
                                val lineText = line.text
//                                Log.i(TAG_LINE, "$lineText")





                                matcher.setNewText(lineText)
                                if  (matcher.doesMatchWithMRZPattern()){


                                    Log.d(TAG_TRUE, "inside true")
                                    Log.d(TAG_TRUE, lineText)

                                val documentType = matcher.findDocumentType()
                                Log.d(TAG_TRUE, documentType.toString())
                                val stringToRecognize = matcher.textToParseBasedOnDocumentType()
                                val stringGenerator = GenerateDataBasedOnMRZString(stringToRecognize!!, documentType!!)

                                Log.d(TAG_TRUE, "Document: ${stringGenerator.getDocumentNumber()}")
                                Log.d(TAG_TRUE, "BIRTH: ${stringGenerator.getBirthDate()}")
                                Log.d(TAG_TRUE, "Expiration: ${stringGenerator.getExpirationDate()}")
                                setNonNullDataToViewModel(stringGenerator)
                                }
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