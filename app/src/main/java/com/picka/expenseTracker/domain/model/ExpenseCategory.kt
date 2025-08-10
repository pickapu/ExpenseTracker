package com.picka.expenseTracker.domain.model

enum class ExpenseCategory(val displayName: String, val color: Long) {
    STAFF("Staff", 0xFFFF6B6B),
    TRAVEL("Travel", 0xFF4ECDC4),
    FOOD("Food", 0xFF45B7D1),
    UTILITY("Utility", 0xFF96CEB4)
}