package com.jenugumpu.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jenugumpu.app.data.repository.HoneyRepository

/**
 * Factory for creating ViewModels with repository dependency
 * Required because ViewModels need custom constructor parameters
 */
class ViewModelFactory(
    private val repository: HoneyRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HarvestViewModel::class.java) -> {
                HarvestViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StockViewModel::class.java) -> {
                StockViewModel(repository) as T
            }
            modelClass.isAssignableFrom(QRScannerViewModel::class.java) -> {
                QRScannerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel() as T
            }
            modelClass.isAssignableFrom(ProfitViewModel::class.java) -> {
                ProfitViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
