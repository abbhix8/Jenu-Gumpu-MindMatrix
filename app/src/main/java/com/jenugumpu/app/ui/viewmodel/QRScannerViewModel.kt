package com.jenugumpu.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenugumpu.app.data.entity.BatchWithLogs
import com.jenugumpu.app.data.repository.HoneyRepository
import com.jenugumpu.app.utils.BatchQRData
import com.jenugumpu.app.utils.QRCodeGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for QR Scanner Screen
 * Handles QR code scanning and batch lookup
 */
class QRScannerViewModel(
    private val repository: HoneyRepository
) : ViewModel() {

    private val _scannedBatch = MutableStateFlow<BatchWithLogs?>(null)
    val scannedBatch: StateFlow<BatchWithLogs?> = _scannedBatch

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Process scanned QR code
     * Parse content and fetch batch from database
     */
    fun processQRCode(qrContent: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Parse QR code JSON
                val qrData: BatchQRData? = QRCodeGenerator.parseBatchQRCode(qrContent)

                if (qrData == null) {
                    _errorMessage.value = "Invalid QR code format"
                    _scannedBatch.value = null
                    return@launch
                }

                // Fetch batch from database
                val batch = repository.getBatchWithLogs(qrData.batchId)

                if (batch == null) {
                    _errorMessage.value = "Batch not found: ${qrData.batchId}"
                    _scannedBatch.value = null
                } else {
                    _scannedBatch.value = batch
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error processing QR code: ${e.message}"
                _scannedBatch.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetScannedBatch() {
        _scannedBatch.value = null
        _errorMessage.value = null
    }
}
