package com.jenugumpu.app.data.dao

import androidx.room.*
import com.jenugumpu.app.data.entity.FloralSource
import com.jenugumpu.app.data.entity.HarvestLog
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for HarvestLog table
 * All operations return Flow for reactive UI updates
 */
@Dao
interface HarvestLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvestLog(harvestLog: HarvestLog): Long

    @Update
    suspend fun updateHarvestLog(harvestLog: HarvestLog)

    @Delete
    suspend fun deleteHarvestLog(harvestLog: HarvestLog)

    @Query("SELECT * FROM harvest_logs ORDER BY date DESC")
    fun getAllHarvestLogs(): Flow<List<HarvestLog>>

    @Query("SELECT * FROM harvest_logs WHERE id = :id")
    suspend fun getHarvestLogById(id: Long): HarvestLog?

    @Query("SELECT * FROM harvest_logs WHERE isUsedInBatch = 0 ORDER BY date DESC")
    fun getUnusedHarvestLogs(): Flow<List<HarvestLog>>

    @Query("SELECT SUM(quantity) FROM harvest_logs WHERE isUsedInBatch = 0")
    fun getTotalAvailableStock(): Flow<Double?>

    @Query("SELECT SUM(quantity) FROM harvest_logs WHERE floralSource = :source AND isUsedInBatch = 0")
    fun getStockByFloralSource(source: FloralSource): Flow<Double?>

    @Query("UPDATE harvest_logs SET isUsedInBatch = 1 WHERE id IN (:logIds)")
    suspend fun markLogsAsUsed(logIds: List<Long>)
}
