package com.leithcarsreports.presentation.reports

import com.leithcarsreports.data.database.dto.BranchCarsLocalDTO
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.data.database.dto.CarReportLocalDTO

data class ReportsUIState(
    val isLoading: Boolean = true,
    val branchesReports: List<BranchReportLocalDTO> = emptyList(),
    val carsReports: List<CarReportLocalDTO> = emptyList(),
    val branchesCarsReports: List<BranchCarsLocalDTO> = emptyList(),
    val buttonText: String = "Upload",
    val toDate: String = "",
    val fromDate: String = "",
)