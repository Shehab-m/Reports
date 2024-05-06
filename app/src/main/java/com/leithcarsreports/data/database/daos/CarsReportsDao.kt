package com.leithcarsreports.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leithcarsreports.data.database.dto.CarReportLocalDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CarsReportsDao {
    @Query("DELETE FROM cars_table WHERE carReportId = :carReportId")
    suspend fun deleteCarReportById(carReportId: Int)

    @Query("DELETE FROM cars_table")
    suspend fun deleteCarReports()

    @Query("SELECT * FROM cars_table where carReportId = :carReportId ")
    fun getCarReportById(carReportId: Int): CarReportLocalDTO?

    @Query("SELECT * FROM cars_table")
    fun getCarsReports(): Flow<List<CarReportLocalDTO>>

    @Insert
    suspend fun insertCarReport(report : CarReportLocalDTO)

    @Insert
    suspend fun insertCarReports(reports : List<CarReportLocalDTO>)

    @Query("SELECT * FROM cars_table WHERE createdAt >= :startDate AND createdAt <= :endDate")
    fun getReportsBetweenDates(startDate: Long, endDate: Long): Flow<List<CarReportLocalDTO>>
}