package com.picka.expenseTracker.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picka.expenseTracker.domain.model.Expense
import com.picka.expenseTracker.domain.model.FilterType
import com.picka.expenseTracker.domain.model.GroupBy
import com.picka.expenseTracker.domain.repository.ExpenseRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<FilterType>(FilterType.Today)
    val selectedFilter: StateFlow<FilterType> = _selectedFilter.asStateFlow()

    private val _groupBy = MutableStateFlow<GroupBy>(GroupBy.Date)
    val groupBy: StateFlow<GroupBy> = _groupBy.asStateFlow()

    private val _todayTotal = MutableStateFlow(0.0)
    val todayTotal: StateFlow<Double> = _todayTotal.asStateFlow()

    val filteredExpenses = combine(
        selectedFilter,
        searchQuery
    ) { filter, query ->
        Pair(filter, query)
    }.flatMapLatest { (filter, query) ->
        when (filter) {
            is FilterType.Today -> repository.getExpensesByDate(LocalDate.now())
            is FilterType.DateRange -> repository.getExpensesBetweenDates(filter.startDate, filter.endDate)
            is FilterType.Category -> repository.getExpensesByCategory(filter.category)
            else -> repository.getAllExpenses()
        }.map { expenses ->
            if (query.isBlank()) {
                expenses
            } else {
                expenses.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.notes?.contains(query, ignoreCase = true) == true ||
                            it.category.displayName.contains(query, ignoreCase = true)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadTodayTotal()
    }

    private fun loadTodayTotal() {
        viewModelScope.launch {
            val total = repository.getTotalForDate(LocalDate.now())
            _todayTotal.value = total
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Check for duplicates
                if (checkDuplicate(expense)) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Similar expense already exists today"
                    )
                    return@launch
                }

                repository.insertExpense(expense)
                loadTodayTotal()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to add expense: ${e.message}"
                )
            }
        }
    }

    private suspend fun checkDuplicate(expense: Expense): Boolean {
        val todayExpenses = repository.getExpensesByDate(LocalDate.now()).first()
        return todayExpenses.any { existing ->
            existing.title.equals(expense.title, ignoreCase = true) &&
                    existing.category == expense.category &&
                    kotlin.math.abs(existing.amount - expense.amount) < 0.01
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.updateExpense(expense)
                loadTodayTotal()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update expense: ${e.message}"
                )
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expense)
                loadTodayTotal()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete expense: ${e.message}"
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: FilterType) {
        _selectedFilter.value = filter
    }

    fun setGroupBy(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(showSuccess = false)
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
}

data class ExpenseUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSuccess: Boolean = false
)
