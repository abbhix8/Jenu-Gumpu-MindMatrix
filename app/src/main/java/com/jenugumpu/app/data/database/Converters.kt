package com.jenugumpu.app.data.database

import androidx.room.TypeConverter
import com.jenugumpu.app.data.entity.BatchStatus
import com.jenugumpu.app.data.entity.FloralSource

/**
 * Room type converters for enum classes
 * Allows Room to store enums as strings in SQLite
 */
class Converters {

    @TypeConverter
    fun fromFloralSource(value: FloralSource): String {
        return value.name
    }

    @TypeConverter
    fun toFloralSource(value: String): FloralSource {
        return FloralSource.valueOf(value)
    }

    @TypeConverter
    fun fromBatchStatus(value: BatchStatus): String {
        return value.name
    }

    @TypeConverter
    fun toBatchStatus(value: String): BatchStatus {
        return BatchStatus.valueOf(value)
    }
}
