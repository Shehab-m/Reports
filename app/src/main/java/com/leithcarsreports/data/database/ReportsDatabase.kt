package com.leithcarsreports.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.data.database.daos.BranchesReportsDao

@Database(
    entities = [BranchReportLocalDTO::class
//    ,CarsReportsLocalDTO::class
    ], version = 1
)

abstract class ReportsDatabase : RoomDatabase() {
    abstract fun getBranchesReportsDao(): BranchesReportsDao
}
