package com.jenugumpu.app.data.repository

import com.jenugumpu.app.data.dao.BatchDao
import com.jenugumpu.app.data.dao.HarvestLogDao
import com.jenugumpu.app.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository layer - Single source of truth for data
 * Abstracts database operations from ViewModels
 * All operations are suspending functions or Flow for reactive updates
 */
class HoneyRepository(
    private val harvestLogDao: HarvestLogDao,
    private val batchDao: BatchDao
) {

    // ========== HARVEST LOG OPERATIONS ==========

    /**
     * Insert a new harvest log
     * Returns the ID of the inserted log
     */
    suspend fun insertHarvestLog(harvestLog: HarvestLog): Long {
        return harvestLogDao.insertHarvestLog(harvestLog)
    }

    /**
     * Get all harvest logs as Flow
     * UI will automatically update when data changes
     */
    fun getAllHarvestLogs(): Flow<List<HarvestLog>> {
        return harvestLogDao.getAllHarvestLogs()
    }

    /**
     * Get harvest logs that haven't been added to any batch
     */
    fun getUnusedHarvestLogs(): Flow<List<HarvestLog>> {
        return harvestLogDao.getUnusedHarvestLogs()
    }

    /**
     * Get total available stock (not in batches)
     * Returns 0.0 if null
     */
    fun getTotalAvailableStock(): Flow<Double> {
        return harvestLogDao.getTotalAvailableStock().map { it ?: 0.0 }
    }

    /**
     * Get stock breakdown by floral source
     */
    fun getStockByFloralSource(source: FloralSource): Flow<Double> {
        return harvestLogDao.getStockByFloralSource(source).map { it ?: 0.0 }
    }

    suspend fun updateHarvestLog(harvestLog: HarvestLog) {
        harvestLogDao.updateHarvestLog(harvestLog)
    }

    suspend fun deleteHarvestLog(harvestLog: HarvestLog) {
        harvestLogDao.deleteHarvestLog(harvestLog)
    }

    // ========== BATCH OPERATIONS ==========

    /**
     * Create a batch from selected harvest logs
     * Automatically creates cross-references and marks logs as used
     */
    suspend fun createBatch(
        batch: Batch,
        harvestLogIds: List<Long>
    ) {
        // Insert the batch
        batchDao.insertBatch(batch)

        // Create cross-references
        harvestLogIds.forEach { logId ->
            batchDao.insertBatchLogCrossRef(
                BatchLogCrossRef(
                    batchId = batch.batchId,
                    harvestLogId = logId
                )
            )
        }

        // Mark logs as used
        harvestLogDao.markLogsAsUsed(harvestLogIds)
    }

    /**
     * Get all batches
     */
    fun getAllBatches(): Flow<List<Batch>> {
        return batchDao.getAllBatches()
    }

    /**
     * Get batch with its associated harvest logs
     */
    suspend fun getBatchWithLogs(batchId: String): BatchWithLogs? {
        return batchDao.getBatchWithLogs(batchId)
    }

    /**
     * Get all batches with their logs
     */
    fun getAllBatchesWithLogs(): Flow<List<BatchWithLogs>> {
        return batchDao.getAllBatchesWithLogs()
    }

    /**
     * Update batch status (COLLECTED → PACKED → READY → SOLD)
     */
    suspend fun updateBatchStatus(batchId: String, status: BatchStatus) {
        batchDao.updateBatchStatus(batchId, status)
    }

    /**
     * Get batch by ID
     */
    suspend fun getBatchById(batchId: String): Batch? {
        return batchDao.getBatchById(batchId)
    }

    /**
     * Get total stock in batches (not sold)
     */
    fun getTotalBatchStock(): Flow<Double> {
        return batchDao.getTotalBatchStock().map { it ?: 0.0 }
    }

    suspend fun updateBatch(batch: Batch) {
        batchDao.updateBatch(batch)
    }

    suspend fun deleteBatch(batch: Batch) {
        batchDao.deleteBatch(batch)
    }
}
