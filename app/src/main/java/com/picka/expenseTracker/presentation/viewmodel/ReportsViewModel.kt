package com.picka.expenseTracker.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picka.expenseTracker.domain.model.CategorySummary
import com.picka.expenseTracker.domain.model.DailySummary
import com.picka.expenseTracker.domain.repository.ExpenseRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(7) // Last 7 days
    val selectedPeriod: StateFlow<Int> = _selectedPeriod.asStateFlow()

    init {
        loadReportsData()
    }

    private fun loadReportsData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val endDate = LocalDate.now()
                val startDate = endDate.minusDays(_selectedPeriod.value.toLong() - 1)

                val categorySummary = repository.getCategorySummary(startDate, endDate)
                val dailySummary = repository.getDailySummary(startDate, endDate)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categorySummary = categorySummary,
                    dailySummary = dailySummary,
                    totalAmount = categorySummary.sumOf { it.totalAmount }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load reports: ${e.message}"
                )
            }
        }
    }

    fun setPeriod(days: Int) {
        _selectedPeriod.value = days
        loadReportsData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun simulateExport(type: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                // Simulate export delay
                kotlinx.coroutines.delay(2000)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Export failed: ${e.message}"
                )
            }
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(showSuccess = false)
    }
}

data class ReportsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSuccess: Boolean = false,
    val categorySummary: List<CategorySummary> = emptyList(),
    val dailySummary: List<DailySummary> = emptyList(),
    val totalAmount: Double = 0.0
)
