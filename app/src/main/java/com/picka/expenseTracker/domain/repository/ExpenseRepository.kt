package com.picka.expenseTracker.domain.repository

import com.picka.expenseTracker.domain.model.CategorySummary
import com.picka.expenseTracker.domain.model.DailySummary
import com.picka.expenseTracker.domain.model.Expense
import com.picka.expenseTracker.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByDate(date: LocalDate): Flow<List<Expense>>
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>
    fun getExpensesBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<Expense>>
    fun searchExpenses(query: String): Flow<List<Expense>>
    suspend fun getTotalForDate(date: LocalDate): Double
    suspend fun insertExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun getCategorySummary(startDate: LocalDate, endDate: LocalDate): List<CategorySummary>
    suspend fun getDailySummary(startDate: LocalDate, endDate: LocalDate): List<DailySummary>
}
