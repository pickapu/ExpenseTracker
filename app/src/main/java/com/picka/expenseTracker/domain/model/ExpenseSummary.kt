package com.picka.expenseTracker.domain.model

data class ExpenseSummary(
    val totalAmount: Double,
    val expenseCount: Int,
    val categoryBreakdown: Map<ExpenseCategory, Double>
)