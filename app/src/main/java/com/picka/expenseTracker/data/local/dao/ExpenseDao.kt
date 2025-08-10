package com.picka.expenseTracker.data.local.dao

import androidx.room.*
import com.picka.expenseTracker.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY createdAt DESC")
    fun getExpensesByDate(date: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY createdAt DESC")
    fun getExpensesByCategory(category: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesBetweenDates(startDate: String, endDate: String): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date = :date")
    suspend fun getTotalForDate(date: String): Double?

    @Query("SELECT SUM(amount) as totalAmount, category FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getCategoryTotals(startDate: String, endDate: String): List<CategoryTotal>

    @Query("SELECT SUM(amount) as totalAmount, date FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date")
    suspend fun getDailyTotals(startDate: String, endDate: String): List<DailyTotal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT * FROM expenses WHERE title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchExpenses(query: String): Flow<List<ExpenseEntity>>
}

data class CategoryTotal(
    val totalAmount: Double,
    val category: String
)

data class DailyTotal(
    val totalAmount: Double,
    val date: String
)
