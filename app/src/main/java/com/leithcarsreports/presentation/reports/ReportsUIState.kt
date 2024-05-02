package com.leithcarsreports.presentation.reports

import com.leithcarsreports.data.database.dto.BranchReportLocalDTO

data class ReportsUIState(
    val isLoading: Boolean = true,
    val reports: List<BranchReportLocalDTO> = emptyList(),
    val buttonText: String = "Click",
    val chartToggle: Boolean = true  // True for the first chart, false for the second

)