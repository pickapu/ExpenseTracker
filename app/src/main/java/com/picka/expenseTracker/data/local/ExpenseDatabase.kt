package com.picka.expenseTracker.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.picka.expenseTracker.data.local.dao.ExpenseDao
import com.picka.expenseTracker.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

}
