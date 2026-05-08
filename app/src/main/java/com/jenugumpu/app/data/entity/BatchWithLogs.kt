package com.jenugumpu.app.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class to retrieve a batch with all its associated harvest logs
 * Room automatically handles the JOIN queries
 */
data class BatchWithLogs(
    @Embedded val batch: Batch,

    @Relation(
        parentColumn = "batchId",
        entityColumn = "id",
        associateBy = Junction(
            value = BatchLogCrossRef::class,
            parentColumn = "batchId",
            entityColumn = "harvestLogId"
        )
    )
    val harvestLogs: List<HarvestLog>
)
