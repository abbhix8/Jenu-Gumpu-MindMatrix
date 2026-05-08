package com.jenugumpu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Represents a batch created from multiple harvest logs
 * Includes QR code data for offline verification
 */
@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey
    val batchId: String = "BATCH-${UUID.randomUUID().toString().take(8).uppercase()}",

    val totalQuantity: Double, // Total kg in this batch

    val grade: String,

    val floralSource: FloralSource,

    val createdDate: Long = Date().time,

    val status: BatchStatus = BatchStatus.COLLECTED,

    val qrCodePath: String = "", // Path to stored QR image (if needed)

    val notes: String = ""
)

enum class BatchStatus {
    COLLECTED,  // Honey collected
    PACKED,     // Honey packed
    READY,      // Ready for sale
    SOLD        // Sold
}
