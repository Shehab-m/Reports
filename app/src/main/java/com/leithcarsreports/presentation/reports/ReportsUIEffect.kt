package com.leithcarsreports.presentation.reports

import com.leithcarsreports.presentation.base.BaseUiEffect

sealed class ReportsUIEffect : BaseUiEffect {
    data class ShowToast(val message: String) : ReportsUIEffect()
}
