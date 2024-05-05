package com.leithcarsreports.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leithcarsreports.data.database.daos.BranchesCarsReportsDao
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.data.database.daos.BranchesReportsDao
import com.leithcarsreports.data.database.daos.CarsReportsDao
import com.leithcarsreports.data.database.dto.BranchCarsLocalDTO
import com.leithcarsreports.data.database.dto.CarReportLocalDTO

@Database(
    entities = [BranchReportLocalDTO::class,CarReportLocalDTO::class,BranchCarsLocalDTO::class], version = 1
)

abstract class ReportsDatabase : RoomDatabase() {
    abstract fun getBranchesReportsDao(): BranchesReportsDao
    abstract fun getCarsReportsDao(): CarsReportsDao
    abstract fun getBranchesCarsReportsDao(): BranchesCarsReportsDao
}
