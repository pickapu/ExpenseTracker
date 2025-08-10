package com.picka.expenseTracker.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val notes: String? = null,
    val hasReceipt: Boolean = false,
    val receiptPath: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val date: LocalDate
) : Parcelable












