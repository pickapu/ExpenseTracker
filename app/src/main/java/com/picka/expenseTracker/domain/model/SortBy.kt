package com.picka.expenseTracker.domain.model


sealed class SortBy {
    object DateDesc : SortBy()
    object DateAsc : SortBy()
    object AmountDesc : SortBy()
    object AmountAsc : SortBy()
}