package com.picka.expenseTracker.domain.model

sealed class GroupBy {
    object Category : GroupBy()
    object Date : GroupBy()
    object None : GroupBy()
}