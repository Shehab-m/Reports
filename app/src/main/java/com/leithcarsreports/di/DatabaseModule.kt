package com.leithcarsreports.di

import android.content.Context
import androidx.room.Room
import com.leithcarsreports.data.database.daos.BranchesReportsDao
import com.leithcarsreports.data.database.ReportsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideReportsDatabase(@ApplicationContext context: Context): ReportsDatabase =
        Room.databaseBuilder(context, ReportsDatabase::class.java, "REPORTS_DATABASE").build()

    @Singleton
    @Provides
    fun provideBranchesReportsDoa(reportsDatabase: ReportsDatabase): BranchesReportsDao = reportsDatabase.getBranchesReportsDao()

}
