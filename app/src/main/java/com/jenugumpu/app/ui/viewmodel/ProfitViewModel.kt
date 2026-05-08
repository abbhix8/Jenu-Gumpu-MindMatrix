package com.jenugumpu.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for Profit Calculator Screen
 * Simple calculation logic
 */
class ProfitViewModel : ViewModel() {

    val quantity = MutableStateFlow("")
    val costPerKg = MutableStateFlow("")
    val sellingPricePerKg = MutableStateFlow("")
    val filteringCostPerKg = MutableStateFlow("")

    private val _totalCost = MutableStateFlow(0.0)
    val totalCost: StateFlow<Double> = _totalCost

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue

    private val _totalProfit = MutableStateFlow(0.0)
    val totalProfit: StateFlow<Double> = _totalProfit

    private val _profitPerKg = MutableStateFlow(0.0)
    val profitPerKg: StateFlow<Double> = _profitPerKg

    private val _totalFilteringCost = MutableStateFlow(0.0)
    val totalFilteringCost: StateFlow<Double> = _totalFilteringCost

    private val _earningsAfterFiltering = MutableStateFlow(0.0)
    val earningsAfterFiltering: StateFlow<Double> = _earningsAfterFiltering

    /**
     * Calculate profit based on inputs
     * Includes filtering/processing costs
     */
    fun calculateProfit() {
        val qty = quantity.value.toDoubleOrNull() ?: 0.0
        val cost = costPerKg.value.toDoubleOrNull() ?: 0.0
        val selling = sellingPricePerKg.value.toDoubleOrNull() ?: 0.0
        val filtering = filteringCostPerKg.value.toDoubleOrNull() ?: 0.0

        _totalCost.value = qty * cost
        _totalFilteringCost.value = qty * filtering
        _totalRevenue.value = qty * selling
        _totalProfit.value = _totalRevenue.value - _totalCost.value
        _earningsAfterFiltering.value = _totalProfit.value - _totalFilteringCost.value
        _profitPerKg.value = if (qty > 0) _earningsAfterFiltering.value / qty else 0.0
    }

    fun reset() {
        quantity.value = ""
        costPerKg.value = ""
        sellingPricePerKg.value = ""
        filteringCostPerKg.value = ""
        _totalCost.value = 0.0
        _totalRevenue.value = 0.0
        _totalProfit.value = 0.0
        _profitPerKg.value = 0.0
        _totalFilteringCost.value = 0.0
        _earningsAfterFiltering.value = 0.0
    }
}
