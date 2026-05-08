package com.jenugumpu.app.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.json.JSONObject

/**
 * Offline QR code generator using ZXing library
 * No internet required
 */
object QRCodeGenerator {

    /**
     * Generate QR code bitmap from batch data
     *
     * QR CODE CONTENT FORMAT (JSON):
     * {
     *   "batchId": "BATCH-ABC123",
     *   "floralSource": "COFFEE",
     *   "grade": "Medium",
     *   "quantity": 25.5,
     *   "date": "2026-05-02"
     * }
     *
     * @param size: QR code size in pixels
     * @return Bitmap ready to display or save
     */
    fun generateBatchQRCode(
        batchId: String,
        floralSource: String,
        grade: String,
        quantity: Double,
        date: String,
        size: Int = 512
    ): Bitmap {
        // Create JSON content for QR code
        val jsonContent = JSONObject().apply {
            put("batchId", batchId)
            put("floralSource", floralSource)
            put("grade", grade)
            put("quantity", quantity)
            put("date", date)
            put("app", "JenuGumpu")
        }.toString()

        return generateQRCode(jsonContent, size)
    }

    /**
     * Generate QR code from any string content
     */
    fun generateQRCode(content: String, size: Int = 512): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, 1)
        }

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    /**
     * Parse batch data from QR code JSON string
     * Returns null if invalid format
     */
    fun parseBatchQRCode(qrContent: String): BatchQRData? {
        return try {
            val json = JSONObject(qrContent)

            // Verify it's a JenuGumpu QR code
            if (json.optString("app") != "JenuGumpu") {
                return null
            }

            BatchQRData(
                batchId = json.getString("batchId"),
                floralSource = json.getString("floralSource"),
                grade = json.getString("grade"),
                quantity = json.getDouble("quantity"),
                date = json.getString("date")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * Data class for parsed QR code content
 */
data class BatchQRData(
    val batchId: String,
    val floralSource: String,
    val grade: String,
    val quantity: Double,
    val date: String
)
