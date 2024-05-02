package com.leithcarsreports.data.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("branches_table")
data class BranchReportLocalDTO(
    @PrimaryKey(autoGenerate = true)
    val branchReportId: Int = 0,
    val branchName: String,
    val branchSalesValue: Double,
)