package com.jenugumpu.app.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenugumpu.app.ml.ClassificationResult
import com.jenugumpu.app.ml.HoneyGradeClassifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Camera + AI Grading Screen
 * Manages camera capture and TFLite inference
 */
class CameraViewModel : ViewModel() {

    private var classifier: HoneyGradeClassifier? = null

    private val _classificationResult = MutableStateFlow<ClassificationResult?>(null)
    val classificationResult: StateFlow<ClassificationResult?> = _classificationResult

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    /**
     * Initialize TFLite classifier
     * Call this from the UI when screen is created
     */
    fun initializeClassifier(context: Context) {
        if (classifier == null) {
            classifier = HoneyGradeClassifier(context)
        }
    }

    /**
     * Classify captured image
     * Runs inference on background thread
     */
    fun classifyImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _isProcessing.value = true

            try {
                val result = classifier?.classifyImage(bitmap)
                _classificationResult.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _classificationResult.value = null
            } finally {
                _isProcessing.value = false
            }
        }
    }

    /**
     * Reset classification result
     */
    fun resetResult() {
        _classificationResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        classifier?.close()
    }
}
