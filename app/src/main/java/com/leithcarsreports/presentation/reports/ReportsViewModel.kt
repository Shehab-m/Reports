package com.leithcarsreports.presentation.reports

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leithcarsreports.data.database.daos.BranchesReportsDao
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import javax.inject.Inject
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val branchesReportsDao: BranchesReportsDao
) : BaseViewModel<ReportsUIState, ReportsUIEffect>(ReportsUIState()), ReportsInteractionListener {

    init {
        getChartsData()
    }

    override fun onClickUploadFile(inputStream: InputStream) {
        try {
            inputStream.use { stream ->
                val workbook: Workbook = XSSFWorkbook(stream)
                val sheet: Sheet = workbook.getSheetAt(0)
                val reports = mutableListOf<BranchReportLocalDTO>()
                val branchInfo = listOf(
                    CellIndex(row = 0, column = 3),  // Branch name
                    CellIndex(row = 9, column = 2),  // Total sales
                    CellIndex(row = 10, column = 3), // Branch name
                    CellIndex(row = 17, column = 2), // Total sales
                    CellIndex(row = 18, column = 3), // Branch name
                    CellIndex(row = 26, column = 2), // Total sales
                    CellIndex(row = 28, column = 3), // Branch name
                    CellIndex(row = 36, column = 2), // Total sales
                    CellIndex(row = 37, column = 3), // Branch name
                    CellIndex(row = 39, column = 2)  // Total sales
                )
                for (i in branchInfo.indices step 2) {
                    val branchNameCell: Cell = sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
                    val branchName: String = branchNameCell.stringCellValue
                    val totalSalesCell: Cell = sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
                    val totalSales: Double = totalSalesCell.numericCellValue
                    reports.add(BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales))
                }
                viewModelScope.launch {
                    saveReportData(reports)
                }
            }
        } catch (e: Exception) {
            Log.e("ReportsViewModel", "Error processing Excel file", e)
        }
    }

    private suspend fun saveReportData(reports: List<BranchReportLocalDTO>) {
        branchesReportsDao.insertBranchesReports(reports)
    }

    private fun getChartsData() {
        viewModelScope.launch {
            branchesReportsDao.getBranchesReports().collectLatest { reports ->
                val combinedReports = combineReportsByBranchName(reports)
                updateState { it.copy(reports = combinedReports) }
            }
        }
    }
    // ViewModel class setup (ensure this logic is appropriately integrated)
    override fun toggleChartDisplay() {
        val currentToggle = state.value.chartToggle
        updateState {
            Log.d("ViewModel", "Toggling chart from ${currentToggle} to ${!currentToggle}")
            it.copy(chartToggle = !currentToggle)
        }
    }

    private fun combineReportsByBranchName(reports: List<BranchReportLocalDTO>): List<BranchReportLocalDTO> {
        val combinedReports = mutableListOf<BranchReportLocalDTO>()
        val branchSalesMap = mutableMapOf<String, Double>()
        reports.forEach { report ->
            val currentSalesValue = branchSalesMap.getOrDefault(report.branchName, 0.0)
            branchSalesMap[report.branchName] = currentSalesValue + report.branchSalesValue
        }
        branchSalesMap.forEach { (branchName, totalSales) ->
            combinedReports.add(BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales))
        }
        return combinedReports
    }

    data class CellIndex(
        val row: Int, val column: Int
    )
}