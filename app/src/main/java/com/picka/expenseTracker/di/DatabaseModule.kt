package com.picka.expenseTracker.di


import android.content.Context
import androidx.room.Room
import com.picka.expenseTracker.data.local.ExpenseDatabase
import com.picka.expenseTracker.data.local.dao.ExpenseDao
import com.picka.expenseTracker.data.repository.ExpenseRepositoryImpl
import com.picka.expenseTracker.domain.repository.ExpenseRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(dao)
    }
}
