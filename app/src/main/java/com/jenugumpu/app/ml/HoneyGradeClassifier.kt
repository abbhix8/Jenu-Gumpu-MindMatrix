package com.jenugumpu.app.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer
import java.nio.ByteOrder


class HoneyGradeClassifier(context: Context) {

    private var interpreter: Interpreter? = null
    private var isModelLoaded = false

    private val labels = listOf("Light", "Medium", "Dark")

    init {
        try {
            // Load the TFLite model from assets folder
            val modelBuffer = FileUtil.loadMappedFile(context, MODEL_PATH)
            interpreter = Interpreter(modelBuffer)
            isModelLoaded = true
        } catch (e: Exception) {
            // Model not found - will use fallback color analysis
            isModelLoaded = false
            e.printStackTrace()
        }
    }


    fun classifyImage(bitmap: Bitmap): ClassificationResult {
        if (!isModelLoaded || interpreter == null) {
            // Fallback: Simple color analysis when model is not available
            return fallbackColorAnalysis(bitmap)
        }

        try {
            // Prepare input tensor
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val resizedImage = ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.BILINEAR)
                .apply(tensorImage)

            // Convert to ByteBuffer
            val inputBuffer = convertBitmapToByteBuffer(resizedImage.bitmap)

            // Output array [1][3] for 3 classes
            val output = Array(1) { FloatArray(labels.size) }

            // Run inference
            interpreter?.run(inputBuffer, output)

            // Find class with highest probability
            val probabilities = output[0]
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
            val confidence = probabilities[maxIndex]

            return ClassificationResult(
                grade = labels[maxIndex],
                confidence = confidence,
                allProbabilities = probabilities.toList()
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return fallbackColorAnalysis(bitmap)
        }
    }

    /**
     * Convert bitmap to ByteBuffer for TFLite input
     * Normalizes pixel values to 0-1 range
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

        for (pixel in pixels) {
            // Normalize RGB values to 0-1
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }

    /**
     * Fallback method when TFLite model is not available
     * Uses simple color analysis to estimate honey grade
     * Dark honey = higher red/brown values
     * Light honey = higher yellow/golden values
     */
    private fun fallbackColorAnalysis(bitmap: Bitmap): ClassificationResult {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

        var totalR = 0f
        var totalG = 0f
        var totalB = 0f
        val pixelCount = 100 * 100

        for (x in 0 until 100) {
            for (y in 0 until 100) {
                val pixel = resizedBitmap.getPixel(x, y)
                totalR += ((pixel shr 16) and 0xFF)
                totalG += ((pixel shr 8) and 0xFF)
                totalB += (pixel and 0xFF)
            }
        }

        val avgR = totalR / pixelCount
        val avgG = totalG / pixelCount
        val avgB = totalB / pixelCount

        // Calculate brightness and color ratio
        val brightness = (avgR + avgG + avgB) / 3
        val redRatio = avgR / (avgG + 1)

        // Classify based on color characteristics
        val (grade, confidence) = when {
            brightness > 180 && redRatio < 1.1 -> "Light" to 0.75f
            brightness < 120 || redRatio > 1.3 -> "Dark" to 0.75f
            else -> "Medium" to 0.70f
        }

        return ClassificationResult(
            grade = grade,
            confidence = confidence,
            allProbabilities = listOf(0.33f, 0.33f, 0.34f), // Equal distribution for fallback
            isFallback = true
        )
    }

    fun close() {
        interpreter?.close()
    }

    companion object {
        private const val MODEL_PATH = "honey_grade_model.tflite"
        private const val INPUT_SIZE = 224
    }
}

/**
 * Result of classification
 * @param grade: Light, Medium, or Dark
 * @param confidence: 0.0 to 1.0
 * @param allProbabilities: Probability for each class
 * @param isFallback: True if using color analysis instead of ML model
 */
data class ClassificationResult(
    val grade: String,
    val confidence: Float,
    val allProbabilities: List<Float>,
    val isFallback: Boolean = false
)
