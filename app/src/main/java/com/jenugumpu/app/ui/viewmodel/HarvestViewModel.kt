package com.jenugumpu.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenugumpu.app.data.entity.FloralSource
import com.jenugumpu.app.data.entity.HarvestLog
import com.jenugumpu.app.data.repository.HoneyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Harvest Screen
 * Manages harvest log creation and listing
 * Uses StateFlow for reactive UI updates
 */
class HarvestViewModel(
    private val repository: HoneyRepository
) : ViewModel() {

    // UI State for form inputs
    val quantity = MutableStateFlow("")
    val selectedFloralSource = MutableStateFlow(FloralSource.COFFEE)
    val grade = MutableStateFlow("Medium")
    val location = MutableStateFlow("")
    val notes = MutableStateFlow("")

    // Harvest logs from database (Flow automatically updates UI)
    val harvestLogs: StateFlow<List<HarvestLog>> = repository.getAllHarvestLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Success message state
    val showSuccessMessage = MutableStateFlow(false)

    /**
     * Add new harvest log to database
     * Validates input and resets form on success
     */
    fun addHarvestLog() {
        val qty = quantity.value.toDoubleOrNull() ?: return

        if (qty <= 0) return

        viewModelScope.launch {
            val harvestLog = HarvestLog(
                quantity = qty,
                floralSource = selectedFloralSource.value,
                grade = grade.value,
                location = location.value,
                notes = notes.value
            )

            repository.insertHarvestLog(harvestLog)

            // Reset form
            quantity.value = ""
            location.value = ""
            notes.value = ""

            // Show success message
            showSuccessMessage.value = true
        }
    }

    fun dismissSuccessMessage() {
        showSuccessMessage.value = false
    }

    fun deleteHarvestLog(harvestLog: HarvestLog) {
        viewModelScope.launch {
            repository.deleteHarvestLog(harvestLog)
        }
    }
}
