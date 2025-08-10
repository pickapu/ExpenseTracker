package com.picka.expenseTracker.data.repository



import com.picka.expenseTracker.data.local.dao.ExpenseDao
import com.picka.expenseTracker.data.local.entity.ExpenseEntity
import com.picka.expenseTracker.domain.model.CategorySummary
import com.picka.expenseTracker.domain.model.DailySummary
import com.picka.expenseTracker.domain.model.Expense
import com.picka.expenseTracker.domain.model.ExpenseCategory
import com.picka.expenseTracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getExpensesByDate(date: LocalDate): Flow<List<Expense>> =
        expenseDao.getExpensesByDate(date.toString()).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> =
        expenseDao.getExpensesByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getExpensesBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<Expense>> =
        expenseDao.getExpensesBetweenDates(startDate.toString(), endDate.toString()).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun searchExpenses(query: String): Flow<List<Expense>> =
        expenseDao.searchExpenses(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getTotalForDate(date: LocalDate): Double =
        expenseDao.getTotalForDate(date.toString()) ?: 0.0

    override suspend fun insertExpense(expense: Expense): Long =
        expenseDao.insertExpense(expense.toEntity())

    override suspend fun updateExpense(expense: Expense) =
        expenseDao.updateExpense(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) =
        expenseDao.deleteExpense(expense.toEntity())

    override suspend fun getCategorySummary(startDate: LocalDate, endDate: LocalDate): List<CategorySummary> {
        val categoryTotals = expenseDao.getCategoryTotals(startDate.toString(), endDate.toString())
        val totalAmount = categoryTotals.sumOf { it.totalAmount }

        return categoryTotals.map { total ->
            CategorySummary(
                category = ExpenseCategory.valueOf(total.category),
                totalAmount = total.totalAmount,
                percentage = if (totalAmount > 0) (total.totalAmount / totalAmount * 100).toFloat() else 0f
            )
        }
    }

    override suspend fun getDailySummary(startDate: LocalDate, endDate: LocalDate): List<DailySummary> {
        val dailyTotals = expenseDao.getDailyTotals(startDate.toString(), endDate.toString())
        return dailyTotals.map { total ->
            DailySummary(
                date = LocalDate.parse(total.date),
                totalAmount = total.totalAmount,
                expenseCount = 1 // This would need a separate query to get actual count
            )
        }
    }
}

// Extension functions for mapping between domain and entity
private fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        category = ExpenseCategory.valueOf(category),
        notes = notes,
        hasReceipt = hasReceipt,
        receiptPath = receiptPath,
        createdAt = LocalDateTime.parse(createdAt),
        date = LocalDate.parse(date)
    )
}

private fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category.name,
        notes = notes,
        hasReceipt = hasReceipt,
        receiptPath = receiptPath,
        createdAt = createdAt.toString(),
        date = date.toString()
    )
}
