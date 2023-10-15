package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CameraViewModel : ViewModel() {
    private val _textDetected = MutableLiveData<String>()
    val textDetected: LiveData<String> get() = _textDetected

    fun detectText(image: InputImage) {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                _textDetected.value = visionText.text
            }
            .addOnFailureListener { e ->
                // Handle any errors
            }
    }
}