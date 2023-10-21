package com.example.passportphotocomparisonthesis.Utils.Camera

import androidx.camera.core.ExperimentalGetImage
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

@ExperimentalGetImage class ImageAnalyzer(private val viewModel: CameraViewModel) : ImageAnalysis.Analyzer {

    private val analyzerScope = CoroutineScope(Dispatchers.Default)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//    private val viewModel: CameraViewModel = CameraViewModel()

    override fun analyze(imageProxy: ImageProxy) {
        analyzerScope.launch {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//                viewModel.detectText(image)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->

                        val matcher = PatternMatcher()
                        for (block in visionText.textBlocks) {
                            val blockText = block.text
                            val blockCornerPoints = block.cornerPoints
                            val blockFrame = block.boundingBox


                            for (line in block.lines) {
                                val lineText = line.text
                                matcher.setNewText(lineText)
                                if  (matcher.doesMatchWithMRZPattern()){
                                    val documentType = matcher.findDocumentType()
                                    val stringToRecognize = matcher.textToParseBasedOnDocumentType()
                                    val stringGenerator = GenerateDataBasedOnMRZString(stringToRecognize!!, documentType!!)
                                    stringGenerator.getBirthDate()
                                }


                                val lineCornerPoints = line.cornerPoints
                                val lineFrame = line.boundingBox
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



    fun setNonNullDataToViewModel(dataGenerator: GenerateDataBasedOnMRZString) {
        viewModel.setDocumentNumber(dataGenerator.getDocumentNumber())
        viewModel.setBirthDate(dataGenerator.getBirthDate())
        viewModel.setExpirationDate(dataGenerator.getExpirationDate())
        viewModel.setName(dataGenerator.getName())
        viewModel.setNationality(dataGenerator.getNationality())
        viewModel.setGender(dataGenerator.getGender())
    }
}