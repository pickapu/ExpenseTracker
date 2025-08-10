package com.picka.expenseTracker.domain.model

import java.time.LocalDate

sealed class FilterType {
    object Today : FilterType()
    object Yesterday : FilterType()
    object ThisWeek : FilterType()
    object ThisMonth : FilterType()
    data class DateRange(val startDate: LocalDate, val endDate: LocalDate) : FilterType()
    data class Category(val category: ExpenseCategory) : FilterType()
}