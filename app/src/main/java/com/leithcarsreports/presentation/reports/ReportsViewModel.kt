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
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
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
            val workbook: Workbook = XSSFWorkbook(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0)
            // Iterate over all rows in the sheet
            for (rowIndex in 0 until sheet.physicalNumberOfRows) {
                val row: Row = sheet.getRow(rowIndex)
                // Iterate over all cells in the row
                for (colIndex in 0 until row.physicalNumberOfCells) {
                    val cell: Cell = row.getCell(colIndex)
                    val cellValue: String = when (cell.cellType) {
                        CellType.NUMERIC -> cell.numericCellValue.toString()
                        CellType.STRING -> cell.stringCellValue
                        CellType.BOOLEAN -> cell.booleanCellValue.toString()
                        CellType.FORMULA -> cell.cellFormula
                        else -> ""
                    }
                    // Log cell content
                    Log.d("Cell[$rowIndex][$colIndex]: ", cellValue)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
           inputStream.close()
        }
    }
//    override fun onClickUploadFile(inputStream: InputStream) {
//        try {
//            inputStream.use { stream ->
//                val workbook: Workbook = XSSFWorkbook(stream)
//                val sheet: Sheet = workbook.getSheetAt(0)
//                val reports = mutableListOf<BranchReportLocalDTO>()
//                val branchInfo = listOf(
//                    Branches.ALMNTAKA_ALHORA_NAME,
//                    Branches.ALMNTAKA_ALHORA_SALES,
//                    Branches.KHALDA_MULTI_BRAND_NAME,
//                    Branches.KHALDA_MULTI_BRAND_SALES,
//                    Branches.KHALDA_DONGFENG_NAME,
//                    Branches.KHALDA_DONGFENG_SALES,
//                    Branches.ARBED_NAME,
//                    Branches.ARBED_SALES,
//                    Branches.FLEET_NAME,
//                    Branches.FLEET_SALES,
////                    CellIndex(row = 0, column = 3),  // Branch name
////                    CellIndex(row = 9, column = 2),  // Total sales
////                    CellIndex(row = 10, column = 3), // Branch name
////                    CellIndex(row = 17, column = 2), // Total sales
////                    CellIndex(row = 18, column = 3), // Branch name
////                    CellIndex(row = 26, column = 2), // Total sales
////                    CellIndex(row = 28, column = 3), // Branch name
////                    CellIndex(row = 36, column = 2), // Total sales
////                    CellIndex(row = 37, column = 3), // Branch name
////                    CellIndex(row = 39, column = 2)  // Total sales
//                )
//                for (i in branchInfo.indices step 2) {
//                    val branchNameCell: Cell = sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
//                    val branchName: String = branchNameCell.stringCellValue
//                    val totalSalesCell: Cell = sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
//                    val totalSales: Double = totalSalesCell.numericCellValue
//                    reports.add(BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales))
//                }
//                viewModelScope.launch {
//                    saveReportData(reports)
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("ReportsViewModel", "Error processing Excel file", e)
//        }
//    }
    private suspend fun saveReportData(reports: List<BranchReportLocalDTO>) {
        branchesReportsDao.insertBranchesReports(reports)
    }

    private fun getChartsData() {
        viewModelScope.launch {
            branchesReportsDao.getBranchesReports().collectLatest { reports ->
                val combinedReports = combineReportsByBranchName(reports)
                Log.d("ReportsViewModel", "Combined Reports: ${combinedReports.map { "${it.branchName}: ${it.branchSalesValue}" }}")
                updateState { it.copy(reports = combinedReports) }
            }
        }
    }

    private fun combineReportsByBranchName(reports: List<BranchReportLocalDTO>): List<BranchReportLocalDTO> {
        val combinedReports = mutableListOf<BranchReportLocalDTO>()
        val branchSalesMap = mutableMapOf<String, Double>()
        reports.forEach { report ->
            val currentSalesValue = branchSalesMap.getOrDefault(report.branchName, 0.0)
            branchSalesMap[report.branchName] = currentSalesValue + report.branchSalesValue
            Log.d("ReportsViewModel", "Processing branch: ${report.branchName} with sales: ${report.branchSalesValue}")
        }
        branchSalesMap.forEach { (branchName, totalSales) ->
            combinedReports.add(BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales))
            Log.d("ReportsViewModel", "Final Branch Data: $branchName, Total Sales: $totalSales")
        }
        return combinedReports
    }

    data class CellIndex(
        val row: Int, val column: Int
    )
}

object Branches {
    val ALMNTAKA_ALHORA_NAME = ReportsViewModel.CellIndex(row = 0, column = 3)
    val ALMNTAKA_ALHORA_SALES = ReportsViewModel.CellIndex(row = 9, column = 2)
    val KHALDA_MULTI_BRAND_NAME = ReportsViewModel.CellIndex(row = 10, column = 3)
    val KHALDA_MULTI_BRAND_SALES = ReportsViewModel.CellIndex(row = 17, column = 2)
    val KHALDA_DONGFENG_NAME = ReportsViewModel.CellIndex(row = 18, column = 3)
    val KHALDA_DONGFENG_SALES = ReportsViewModel.CellIndex(row = 26, column = 2)
    val ARBED_NAME = ReportsViewModel.CellIndex(row = 28, column = 3)
    val ARBED_SALES = ReportsViewModel.CellIndex(row = 36, column = 2)
    val FLEET_NAME = ReportsViewModel.CellIndex(row = 37, column = 3)
    val FLEET_SALES = ReportsViewModel. CellIndex(row = 39, column = 2)
}

object AlmntakaAlhora{
    val SalesMan1Name = ReportsViewModel.CellIndex(row = 2, column = 16)
    val SalesMan1Value = ReportsViewModel.CellIndex(row = 2, column = 2)
    val SalesMan2Name = ReportsViewModel.CellIndex(row = 3, column = 16)
    val SalesMan2Value = ReportsViewModel.CellIndex(row = 3, column = 2)
    val SalesMan3Name = ReportsViewModel.CellIndex(row = 4, column = 16)
    val SalesMan3Value = ReportsViewModel.CellIndex(row = 4, column = 2)
    val SalesMan4Name = ReportsViewModel.CellIndex(row = 5, column = 16)
    val SalesMan4Value = ReportsViewModel.CellIndex(row = 5, column = 2)
    val SalesMan5Name = ReportsViewModel.CellIndex(row = 6, column = 16)
    val SalesMan5Value = ReportsViewModel.CellIndex(row = 6, column = 2)
    val SalesMan6Name = ReportsViewModel.CellIndex(row = 7, column = 16)
    val SalesMan6Value = ReportsViewModel.CellIndex(row = 7, column = 2)
    val SalesMan7Name = ReportsViewModel.CellIndex(row = 8, column = 16)
    val SalesMan7Value = ReportsViewModel.CellIndex(row = 8, column = 2)
}

 object KhaldaMulitBrand{
    val SalesMan1Name = ReportsViewModel.CellIndex(row = 12, column = 16)
    val SalesMan1Value = ReportsViewModel.CellIndex(row = 12, column = 2)
    val SalesMan2Name = ReportsViewModel.CellIndex(row = 13, column = 16)
    val SalesMan2Value = ReportsViewModel.CellIndex(row = 13, column = 2)
    val SalesMan3Name = ReportsViewModel.CellIndex(row = 14, column = 16)
    val SalesMan3Value = ReportsViewModel.CellIndex(row = 14, column = 2)
    val SalesMan4Name = ReportsViewModel.CellIndex(row = 15, column = 16)
    val SalesMan4Value = ReportsViewModel.CellIndex(row = 15, column = 2)
    val SalesMan5Name = ReportsViewModel.CellIndex(row = 16, column = 16)
    val SalesMan5Value = ReportsViewModel.CellIndex(row = 16, column = 2)
}

object KkaldaDongFeng{
    val SalesMan1Name = ReportsViewModel.CellIndex(row = 20, column = 16)
    val SalesMan1Value = ReportsViewModel.CellIndex(row = 20, column = 2)
    val SalesMan2Name = ReportsViewModel.CellIndex(row = 21, column = 16)
    val SalesMan2Value = ReportsViewModel.CellIndex(row = 21, column = 2)
    val SalesMan3Name = ReportsViewModel.CellIndex(row = 22, column = 16)
    val SalesMan3Value = ReportsViewModel.CellIndex(row = 22, column = 2)
    val SalesMan4Name = ReportsViewModel.CellIndex(row = 23, column = 16)
    val SalesMan4Value = ReportsViewModel.CellIndex(row = 23, column = 2)
    val SalesMan5Name = ReportsViewModel.CellIndex(row = 24, column = 16)
    val SalesMan5Value = ReportsViewModel.CellIndex(row = 24, column = 2)
    val SalesMan6Name = ReportsViewModel.CellIndex(row = 25, column = 16)
    val SalesMan6Value = ReportsViewModel.CellIndex(row = 25, column = 2)
}

object Arbad {
    val SalesMan1Name = ReportsViewModel.CellIndex(row = 30, column = 16)
    val SalesMan1Value = ReportsViewModel.CellIndex(row = 30, column = 2)
    val SalesMan2Name = ReportsViewModel.CellIndex(row = 31, column = 16)
    val SalesMan2Value = ReportsViewModel.CellIndex(row = 31, column = 2)
    val SalesMan3Name = ReportsViewModel.CellIndex(row = 32, column = 16)
    val SalesMan3Value = ReportsViewModel.CellIndex(row = 32, column = 2)
    val SalesMan4Name = ReportsViewModel.CellIndex(row = 33, column = 16)
    val SalesMan4Value = ReportsViewModel.CellIndex(row = 33, column = 2)
    val SalesMan5Name = ReportsViewModel.CellIndex(row = 34, column = 16)
    val SalesMan5Value = ReportsViewModel.CellIndex(row = 34, column = 2)
    val SalesMan6Name = ReportsViewModel.CellIndex(row = 35, column = 16)
    val SalesMan6Value = ReportsViewModel.CellIndex(row = 35, column = 2)
}

object Fleet{
    val SalesMan1Name = ReportsViewModel.CellIndex(row = 39, column = 16)
    val SalesMan1Value = ReportsViewModel.CellIndex(row = 39, column = 15)
}