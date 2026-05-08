package com.jenugumpu.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenugumpu.app.data.entity.*
import com.jenugumpu.app.data.repository.HoneyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Stock/Batch Management Screen
 * Handles batch creation from harvest logs
 */
class StockViewModel(
    private val repository: HoneyRepository
) : ViewModel() {

    // Available harvest logs (not yet in batches)
    val unusedHarvestLogs: StateFlow<List<HarvestLog>> = repository.getUnusedHarvestLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // All batches with their logs
    val batchesWithLogs: StateFlow<List<BatchWithLogs>> = repository.getAllBatchesWithLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Stock breakdown by floral source
    val coffeeStock: StateFlow<Double> = repository.getStockByFloralSource(FloralSource.COFFEE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val wildflowerStock: StateFlow<Double> = repository.getStockByFloralSource(FloralSource.WILDFLOWER)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val neemStock: StateFlow<Double> = repository.getStockByFloralSource(FloralSource.NEEM)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Total available stock (not in batches)
    val availableStock: StateFlow<Double> = repository.getTotalAvailableStock()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Total stock in batches (not sold)
    val batchStock: StateFlow<Double> = repository.getTotalBatchStock()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // True collective stock (available + batches)
    val totalStock: StateFlow<Double> = combine(availableStock, batchStock) { available, batch ->
        available + batch
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Selected logs for batch creation
    val selectedLogIds = MutableStateFlow<Set<Long>>(emptySet())

    // Batch creation state
    val batchGrade = MutableStateFlow("Medium")
    val batchNotes = MutableStateFlow("")

    /**
     * Toggle log selection for batch creation
     */
    fun toggleLogSelection(logId: Long) {
        val current = selectedLogIds.value.toMutableSet()
        if (current.contains(logId)) {
            current.remove(logId)
        } else {
            current.add(logId)
        }
        selectedLogIds.value = current
    }

    /**
     * Create batch from selected logs
     */
    fun createBatch(onSuccess: (String) -> Unit) {
        if (selectedLogIds.value.isEmpty()) return

        viewModelScope.launch {
            // Calculate total quantity
            val selectedLogs = unusedHarvestLogs.value.filter { it.id in selectedLogIds.value }
            val totalQty = selectedLogs.sumOf { it.quantity }

            // Determine dominant floral source
            val floralSource = selectedLogs
                .groupBy { it.floralSource }
                .maxByOrNull { it.value.size }
                ?.key ?: FloralSource.MIXED

            val batch = Batch(
                totalQuantity = totalQty,
                grade = batchGrade.value,
                floralSource = floralSource,
                notes = batchNotes.value
            )

            repository.createBatch(batch, selectedLogIds.value.toList())

            // Reset
            selectedLogIds.value = emptySet()
            batchNotes.value = ""

            onSuccess(batch.batchId)
        }
    }

    /**
     * Update batch status
     */
    fun updateBatchStatus(batchId: String, newStatus: BatchStatus) {
        viewModelScope.launch {
            repository.updateBatchStatus(batchId, newStatus)
        }
    }
}
