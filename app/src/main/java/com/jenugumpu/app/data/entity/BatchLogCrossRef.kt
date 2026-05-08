package com.jenugumpu.app.data.entity

import androidx.room.Entity

/**
 * Many-to-many relationship between batches and harvest logs
 * Allows tracking which logs make up each batch
 */
@Entity(
    tableName = "batch_log_cross_ref",
    primaryKeys = ["batchId", "harvestLogId"]
)
data class BatchLogCrossRef(
    val batchId: String,
    val harvestLogId: Long
)
