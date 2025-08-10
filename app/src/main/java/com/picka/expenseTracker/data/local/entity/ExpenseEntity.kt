package com.picka.expenseTracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val hasReceipt: Boolean = false,
    val receiptPath: String? = null,
    val createdAt: String = LocalDateTime.now().toString(),
    val date: String
)
