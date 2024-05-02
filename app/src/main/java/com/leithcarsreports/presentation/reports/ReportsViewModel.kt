package com.leithcarsreports.presentation.reports

import androidx.lifecycle.viewModelScope
import com.leithcarsreports.data.database.daos.BranchesReportsDao
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.data.services.IFilePickerManager
import com.leithcarsreports.domain.model.BranchReport
import com.leithcarsreports.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val filePickerManager: IFilePickerManager,
    private val branchesReportsDao: BranchesReportsDao,
) : BaseViewModel<ReportsUIState, ReportsUIEffect>(ReportsUIState()), ReportsInteractionListener {

    init {
        getChartsData()
    }

    override fun onClickUploadFile() {
        val p = filePickerManager.pickExcelFile()
        viewModelScope.launch {
            saveReportData(BranchReport(1, "Giza Branch", 434.5))
        }
    }

    private suspend fun saveReportData(report: BranchReport) {
        branchesReportsDao.insertBranchReport(
            BranchReportLocalDTO(
                branchName = report.branchName, branchSalesValue = report.branchSalesValue
            )
        )
    }

    private fun getChartsData() {
        viewModelScope.launch {
            branchesReportsDao.getBranchesReports().collectLatest { reports ->
                val report = reports //TODO merge branches with same name
                updateState { it.copy(reports = report) }
            }
        }
    }
}