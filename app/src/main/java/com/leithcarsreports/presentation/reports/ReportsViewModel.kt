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
//    private val filePickerManager: IFilePickerManager,
    private val branchesReportsDao: BranchesReportsDao,
) : BaseViewModel<ReportsUIState, ReportsUIEffect>(ReportsUIState()), ReportsInteractionListener {

    init {
        getChartsData()
    }

    //    override fun onClickUploadFile(inputStream: InputStream) {
//        val workbook: Workbook = XSSFWorkbook(inputStream)
//        Log.d("onClickUploadFile: ", "${workbook}")
//        val sheet: Sheet = workbook.getSheetAt(0)
//        Log.d("onClickUploadFile: ", "${sheet}")
//        val rowIndex = 1 // Row index of the data you want to access
//        val colIndex = 1 // Column index of the data you want to access
//        val row: Row = sheet.getRow(rowIndex)
//        Log.d("onClickUploadFile: ", "${row}")
//        val cell: Cell = row.getCell(colIndex)
//        Log.d("onClickUploadFile: ", "${cell}")
//        val value: String = cell.toString() // Assuming the data is a string, adjust accordingly
//        Log.d("onClickUploadFile erbabra: ", value)
//        updateState { it.copy(buttonText = value) }
//        viewModelScope.launch {
//            saveReportData(BranchReport(1, "Giza Branch", 434.5))
//        }
//    }

    override fun onClickUploadFile(inputStream: InputStream) {
        try {
            val reports = mutableListOf<BranchReportLocalDTO>()

            val workbook: Workbook = XSSFWorkbook(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0)

            val branchInfo = listOf(
                CellIndex(row = 0, column = 3),  // Branch name starts at Cell[0][3]
                CellIndex(row = 9, column = 2),  // Total sales starts at Cell[9][2]
                CellIndex(row = 10, column = 3), // Branch name starts at Cell[10][3]
                CellIndex(row = 17, column = 2), // Total sales starts at Cell[17][2]
                CellIndex(row = 18, column = 3), // Branch name starts at Cell[18][3]
                CellIndex(row = 26, column = 2), // Total sales starts at Cell[26][2]
                CellIndex(row = 28, column = 3), // Branch name starts at Cell[28][3]
                CellIndex(row = 36, column = 2), // Total sales starts at Cell[36][2]
                CellIndex(row = 37, column = 3), // Branch name starts at Cell[37][3]
                CellIndex(row = 39, column = 2)  // Total sales starts at Cell[39][2]
            )

            // Iterate over the branch info cell indexes
            for (i in branchInfo.indices step 2) {
                val branchNameCell: Cell = sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
                val branchName: String = branchNameCell.stringCellValue

                val totalSalesCell: Cell = sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
                val totalSales: Double = totalSalesCell.numericCellValue

                // Create BranchReportLocalDTO object and add to list
                val report = BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales)
                reports.add(report)
            }

            // Log the list of reports
            reports.forEachIndexed { index, report ->
                Log.d("Report[$index]: ", "Branch Name: ${report.branchName}, Sales Value: ${report.branchSalesValue}")
            }
            viewModelScope.launch {
                saveReportData(reports)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
    }

    data class BranchInfoIndexes(
        val name: CellIndex, val total: CellIndex
    )

    data class CellIndex(
        val row: Int, val column: Int
    )

//    override fun onClickUploadFile(inputStream: InputStream) {
//        try {
//            val reports = mutableListOf<BranchReportLocalDTO>()
//
//            val workbook: Workbook = XSSFWorkbook(inputStream)
//            val sheet: Sheet = workbook.getSheetAt(0)
//            val repo = listOf(
//                BranchInfoIndexes(name = CellIndex(), total = CellIndex())
//            )
//
//            // Iterate over all rows in the sheet
//            for (rowIndex in 0 until sheet.physicalNumberOfRows) {
//                val row: Row = sheet.getRow(rowIndex)
//                // Iterate over all cells in the row
//                for (colIndex in 0 until row.physicalNumberOfCells) {
//                    val cell: Cell = row.getCell(colIndex)
//                    val cellValue: String = when (cell.cellType) {
//                        CellType.NUMERIC -> cell.numericCellValue.toString()
//                        CellType.STRING -> cell.stringCellValue
//                        CellType.BOOLEAN -> cell.booleanCellValue.toString()
//                        CellType.FORMULA -> cell.cellFormula
//                        else -> ""
//                    }
//                    // Log cell content
//                    Log.d("Cell[$rowIndex][$colIndex]: ", cellValue)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
////            inputStream.close()
//        }
//    }


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

    private fun combineReportsByBranchName(reports: List<BranchReportLocalDTO>): List<BranchReportLocalDTO> {
        val combinedReports = mutableListOf<BranchReportLocalDTO>()

        // Create a map to group reports by branch name and sum up sales values
        val branchSalesMap = mutableMapOf<String, Double>()

        for (report in reports) {
            val branchName = report.branchName
            val salesValue = report.branchSalesValue

            // Update sales value for existing branch or add new entry
            val currentSalesValue = branchSalesMap.getOrDefault(branchName, 0.0)
            branchSalesMap[branchName] = currentSalesValue + salesValue
        }

        // Create combined reports from grouped sales values
        for ((branchName, totalSales) in branchSalesMap) {
            val combinedReport = BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales)
            combinedReports.add(combinedReport)
        }

        return combinedReports
    }

}