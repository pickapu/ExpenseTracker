package com.picka.expenseTracker.domain.model

import java.time.LocalDate

data class DailySummary(
    val date: LocalDate,
    val totalAmount: Double,
    val expenseCount: Int
)