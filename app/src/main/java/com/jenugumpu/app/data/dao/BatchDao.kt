package com.jenugumpu.app.data.dao

import androidx.room.*
import com.jenugumpu.app.data.entity.Batch
import com.jenugumpu.app.data.entity.BatchLogCrossRef
import com.jenugumpu.app.data.entity.BatchStatus
import com.jenugumpu.app.data.entity.BatchWithLogs
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Batch table
 * Handles batch creation and relationship with harvest logs
 */
@Dao
interface BatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: Batch)

    @Update
    suspend fun updateBatch(batch: Batch)

    @Delete
    suspend fun deleteBatch(batch: Batch)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBatchLogCrossRef(crossRef: BatchLogCrossRef)

    @Query("SELECT * FROM batches ORDER BY createdDate DESC")
    fun getAllBatches(): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchId = :batchId")
    suspend fun getBatchById(batchId: String): Batch?

    @Transaction
    @Query("SELECT * FROM batches WHERE batchId = :batchId")
    suspend fun getBatchWithLogs(batchId: String): BatchWithLogs?

    @Transaction
    @Query("SELECT * FROM batches ORDER BY createdDate DESC")
    fun getAllBatchesWithLogs(): Flow<List<BatchWithLogs>>

    @Query("UPDATE batches SET status = :status WHERE batchId = :batchId")
    suspend fun updateBatchStatus(batchId: String, status: BatchStatus)

    @Query("SELECT SUM(totalQuantity) FROM batches WHERE status != 'SOLD'")
    fun getTotalBatchStock(): Flow<Double?>
}
