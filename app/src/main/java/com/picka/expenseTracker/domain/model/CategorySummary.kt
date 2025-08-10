package com.picka.expenseTracker.domain.model

data class CategorySummary(
    val category: ExpenseCategory,
    val totalAmount: Double,
    val percentage: Float
)
