package com.jenugumpu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Represents a single harvest entry logged by a beekeeper
 * All data stored locally in Room database for offline access
 */
@Entity(tableName = "harvest_logs")
data class HarvestLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val quantity: Double, // kg

    val floralSource: FloralSource,

    val grade: String, // Light, Medium, Dark (from AI or manual)

    val location: String = "",

    val date: Long = Date().time, // Timestamp in milliseconds

    val notes: String = "",

    val isUsedInBatch: Boolean = false // Track if this log is already in a batch
)

enum class FloralSource {
    COFFEE,
    WILDFLOWER,
    NEEM,
    MIXED
}
