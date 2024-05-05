package com.leithcarsreports.data.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "cars_table")
@TypeConverters(Converters::class)
data class CarReportLocalDTO(
    @PrimaryKey(autoGenerate = true)
    val carReportId: Int = 0,
    val carName: String,
    val carSalesValue: Double,
    var createdAt: Long = 0
) {
    init {
        createdAt = System.currentTimeMillis()
    }
}
