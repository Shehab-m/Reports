package com.leithcarsreports.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leithcarsreports.data.database.dto.BranchCarsLocalDTO
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import kotlinx.coroutines.flow.Flow
@Dao
interface BranchesCarsReportsDao {
    @Query("DELETE FROM branches_cars_table WHERE branchReportId = :branchId")
    suspend fun deleteBranchesCarsReportById(branchId: Int)

    @Query("DELETE FROM branches_cars_table")
    suspend fun deleteBranchesCarsReports()

    @Query("SELECT * FROM branches_cars_table where branchReportId = :branchId ")
    fun getBranchesCarsById(branchId: Int): BranchCarsLocalDTO?

    @Query("SELECT * FROM branches_cars_table")
    fun getBranchesCarsReports(): Flow<List<BranchCarsLocalDTO>>

    @Insert
    suspend fun insertBranchesCarsReport(report : BranchCarsLocalDTO)

    @Insert
    suspend fun insertBranchesCarsReports(reports : List<BranchCarsLocalDTO>)
}