package com.leithcarsreports.data.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = "branches_cars_table")
@TypeConverters(Converters::class)
data class BranchCarsLocalDTO(
    @PrimaryKey(autoGenerate = true)
    val branchReportId: Int = 0,
    val branchName: String,
    val carName: String,
    val branchSalesValue: Double,
    var createdAt: Long = 0
) {
    init {
        createdAt = System.currentTimeMillis()
    }
}

