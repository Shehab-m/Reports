package com.leithcarsreports.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface BranchesReportsDao {
    @Query("DELETE FROM branches_table WHERE branchReportId = :branchId")
    suspend fun deleteBranchReportById(branchId: Int)

    @Query("DELETE FROM branches_table")
    suspend fun deleteBranchesReports()

    @Query("SELECT * FROM branches_table where branchReportId = :branchId ")
    fun getBranchById(branchId: Int): BranchReportLocalDTO?

    @Query("SELECT * FROM branches_table")
    fun getBranchesReports(): Flow<List<BranchReportLocalDTO>>

    @Insert
    suspend fun insertBranchReport(report : BranchReportLocalDTO)

    @Insert
    suspend fun insertBranchesReports(reports : List<BranchReportLocalDTO>)

    @Query("SELECT * FROM branches_table WHERE createdAt >= :startDate AND createdAt <= :endDate")
    fun getReportsBetweenDates(startDate: Long, endDate: Long): Flow<List<BranchReportLocalDTO>>
}