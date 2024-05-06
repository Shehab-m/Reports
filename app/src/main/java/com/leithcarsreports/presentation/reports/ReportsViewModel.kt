package com.leithcarsreports.presentation.reports

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.leithcarsreports.data.database.daos.BranchesCarsReportsDao
import com.leithcarsreports.data.database.daos.BranchesReportsDao
import com.leithcarsreports.data.database.daos.CarsReportsDao
import com.leithcarsreports.data.database.dto.BranchCarsLocalDTO
import com.leithcarsreports.data.database.dto.BranchReportLocalDTO
import com.leithcarsreports.data.database.dto.CarReportLocalDTO
import com.leithcarsreports.presentation.base.BaseViewModel
import com.leithcarsreports.presentation.utils.Branches
import com.leithcarsreports.presentation.utils.convertStringToTimestamp
import com.leithcarsreports.presentation.utils.isFromDateIsBeforeTheToDate
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
    private val branchesReportsDao: BranchesReportsDao,
    private val carsReportsDao: CarsReportsDao,
    private val branchesCarsReportsDao: BranchesCarsReportsDao,
) : BaseViewModel<ReportsUIState, ReportsUIEffect>(ReportsUIState()), ReportsInteractionListener {

    init {
        getChartsData()
    }

    override fun onChangeFromDate(date: String) {
        updateState { it.copy(fromDate = date) }
    }

    override fun getBranchesWithinDates() {
        val fromDate = convertStringToTimestamp(state.value.fromDate)
        val toDate = convertStringToTimestamp(state.value.toDate)
        viewModelScope.launch {
            branchesReportsDao.getReportsBetweenDates(fromDate,toDate).collectLatest { reports ->
                val combinedReports = combineReportsByBranchName(reports)
                updateState { it.copy(branchesReports = combinedReports) }
            }
        }
    }

    override fun getCarsWithinDates() {
        val fromDate = convertStringToTimestamp(state.value.fromDate)
        val toDate = convertStringToTimestamp(state.value.toDate)
        viewModelScope.launch {
            carsReportsDao.getReportsBetweenDates(fromDate,toDate).collectLatest { reports ->
                val combinedReports = combineReportsByCarName(reports)
                updateState { it.copy(carsReports = combinedReports) }
            }
        }
    }

    override fun getBranchesCarsWithinDates() {
        val fromDate = convertStringToTimestamp(state.value.fromDate)
        val toDate = convertStringToTimestamp(state.value.toDate)
        viewModelScope.launch {
            branchesCarsReportsDao.getReportsBetweenDates(fromDate,toDate).collectLatest { reports ->
                val combinedReports = combineReportsByBranchAndCarName(reports)
                updateState { it.copy(branchesCarsReports = combinedReports) }
            }
        }
    }

    override fun onChangeToDate(date: String) {
        if (state.value.fromDate.isEmpty()) {
            sendEffect(ReportsUIEffect.ShowToast("Please, select from date first"))
        } else if (!isFromDateIsBeforeTheToDate(state.value.fromDate, date)) {
            sendEffect(ReportsUIEffect.ShowToast("Sorry, you should make this date after the ${state.value.fromDate}"))
        } else {
            updateState { it.copy(toDate = date) }
        }

    }


    override fun onClickUploadFileBranches(inputStream: InputStream) {
        try {
            inputStream.use { stream ->
                val workbook: Workbook = XSSFWorkbook(stream)
                val sheet: Sheet = workbook.getSheetAt(0)
                val reports = mutableListOf<BranchReportLocalDTO>()
                val branchInfo = listOf(
                    Branches.ALMNTAKA_ALHORA_NAME,
                    Branches.ALMNTAKA_ALHORA_SALES,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    Branches.KHALDA_MULTI_BRAND_SALES,
                    Branches.KHALDA_DONGFENG_NAME,
                    Branches.KHALDA_DONGFENG_SALES,
                    Branches.ARBED_NAME,
                    Branches.ARBED_SALES,
                    Branches.FLEET_NAME,
                    Branches.FLEET_SALES,
                )
                for (i in branchInfo.indices step 2) {
                    val branchNameCell: Cell =
                        sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
                    val branchName: String = branchNameCell.stringCellValue
                    val totalSalesCell: Cell =
                        sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
                    val totalSales: Double = totalSalesCell.numericCellValue
                    reports.add(
                        BranchReportLocalDTO(
                            branchName = branchName, branchSalesValue = totalSales
                        )
                    )
                }
                viewModelScope.launch {
                    saveBranchesReportData(reports)
                }
            }
        } catch (e: Exception) {
            Log.e("ReportsViewModel", "Error processing Excel file", e)
        }
    }

    override fun onClickUploadFileCars(inputStream: InputStream) {
        try {
            inputStream.use { stream ->
                val workbook: Workbook = XSSFWorkbook(stream)
                val sheet: Sheet = workbook.getSheetAt(0)
                val reports = mutableListOf<CarReportLocalDTO>()
                val branchInfo = listOf(
                    TotalCarsSells.DongFengName,
                    TotalCarsSells.DongFengValue,
                    TotalCarsSells.BydName,
                    TotalCarsSells.BydValue,
                    TotalCarsSells.VwID3_4_6Name,
                    TotalCarsSells.VwID3_4_6Value,
                    TotalCarsSells.Honda_E_Ns1Name,
                    TotalCarsSells.Honda_E_Ns1Value,
                    TotalCarsSells.UsedName,
                    TotalCarsSells.UsedValue,
                    TotalCarsSells.WulingName,
                    TotalCarsSells.WulingValue,
                    TotalCarsSells.JmevName,
                    TotalCarsSells.JmevValue,
                    TotalCarsSells.JacName,
                    TotalCarsSells.JacValue,
                    TotalCarsSells.MgName,
                    TotalCarsSells.MgValue,
                )
                for (i in branchInfo.indices step 2) {
                    Log.d("onClickUploadFileCars evveverve: ", i.toString())
                    val branchNameCell: Cell =
                        sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
                    val branchName: String = branchNameCell.stringCellValue
                    val totalSalesCell: Cell =
                        sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
                    val totalSales: Double = totalSalesCell.numericCellValue
                    reports.add(CarReportLocalDTO(carName = branchName, carSalesValue = totalSales))
                }
                viewModelScope.launch {
                    Log.d("onClickUploadFileCars: ", "viewModelScope")
                    carsReportsDao.insertCarReports(reports)
                }
            }
        } catch (e: Exception) {
            Log.e("ReportsViewModel", "Error processing Excel file", e)
        }
    }


    override fun onClickUploadFileBranchesCars(inputStream: InputStream) {
        try {
            inputStream.use { stream ->
                val workbook: Workbook = XSSFWorkbook(stream)
                val sheet: Sheet = workbook.getSheetAt(0)
                val reports = mutableListOf<BranchCarsLocalDTO>()

                val branchCarInfo = listOf(
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.DongFengName,
                    AlmntakaAlhora.DongFengValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.BydName,
                    AlmntakaAlhora.BydValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.VwID3_4_6Name,
                    AlmntakaAlhora.VwID3_4_6Value,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.Honda_E_Ns1Name,
                    AlmntakaAlhora.Honda_E_Ns1Value,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.UsedName,
                    AlmntakaAlhora.UsedValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.WulingName,
                    AlmntakaAlhora.WulingValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.JmevName,
                    AlmntakaAlhora.JmevValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.JacName,
                    AlmntakaAlhora.JacValue,
                    Branches.ALMNTAKA_ALHORA_NAME,
                    AlmntakaAlhora.MgName,
                    AlmntakaAlhora.MgValue,

                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.DongFengName,
                    KhaldaMulitBrand.DongFengValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.BydName,
                    KhaldaMulitBrand.BydValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.VwID3_4_6Name,
                    KhaldaMulitBrand.VwID3_4_6Value,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.Honda_E_Ns1Name,
                    KhaldaMulitBrand.Honda_E_Ns1Value,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.UsedName,
                    KhaldaMulitBrand.UsedValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.WulingName,
                    KhaldaMulitBrand.WulingValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.JmevName,
                    KhaldaMulitBrand.JmevValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.JacName,
                    KhaldaMulitBrand.JacValue,
                    Branches.KHALDA_MULTI_BRAND_NAME,
                    KhaldaMulitBrand.MgName,
                    KhaldaMulitBrand.MgValue,

                    Branches.ARBED_NAME,
                    Arbad.DongFengName,
                    Arbad.DongFengValue,
                    Branches.ARBED_NAME,
                    Arbad.BydName,
                    Arbad.BydValue,
                    Branches.ARBED_NAME,
                    Arbad.VwID3_4_6Name,
                    Arbad.VwID3_4_6Value,
                    Branches.ARBED_NAME,
                    Arbad.Honda_E_Ns1Name,
                    Arbad.Honda_E_Ns1Value,
                    Branches.ARBED_NAME,
                    Arbad.UsedName,
                    Arbad.UsedValue,
                    Branches.ARBED_NAME,
                    Arbad.WulingName,
                    Arbad.WulingValue,
                    Branches.ARBED_NAME,
                    Arbad.JmevName,
                    Arbad.JmevValue,
                    Branches.ARBED_NAME,
                    Arbad.JacName,
                    Arbad.JacValue,
                    Branches.ARBED_NAME,
                    Arbad.MgName,
                    Arbad.MgValue,

                    Branches.FLEET_NAME, Fleet.DongFengName, Fleet.DongFengValue,
                    Branches.FLEET_NAME, Fleet.BydName, Fleet.BydValue,
                    Branches.FLEET_NAME, Fleet.VwID3_4_6Name, Fleet.VwID3_4_6Value,
                    Branches.FLEET_NAME, Fleet.Honda_E_Ns1Name, Fleet.Honda_E_Ns1Value,
                    Branches.FLEET_NAME, Fleet.UsedName, Fleet.UsedValue,
                    Branches.FLEET_NAME, Fleet.WulingName, Fleet.WulingValue,
                    Branches.FLEET_NAME, Fleet.JmevName, Fleet.JmevValue,
                    Branches.FLEET_NAME, Fleet.JacName, Fleet.JacValue,
                    Branches.FLEET_NAME, Fleet.MgName, Fleet.MgValue,

                    )

                for (i in branchCarInfo.indices step 3) {
                    val branchNameCell: Cell =
                        sheet.getRow(branchCarInfo[i].row).getCell(branchCarInfo[i].column)
                    val branchName: String = branchNameCell.stringCellValue
                    val carNameCell: Cell =
                        sheet.getRow(branchCarInfo[i + 1].row).getCell(branchCarInfo[i + 1].column)
                    val carName: String = carNameCell.stringCellValue
                    val carSalesValueCell: Cell =
                        sheet.getRow(branchCarInfo[i + 2].row).getCell(branchCarInfo[i + 2].column)
                    val carSalesValue: Double = carSalesValueCell.numericCellValue

                    reports.add(
                        BranchCarsLocalDTO(
                            branchName = branchName,
                            carName = carName,
                            branchSalesValue = carSalesValue
                        )
                    )
                }
                viewModelScope.launch {
                    branchesCarsReportsDao.insertBranchesCarsReports(reports)
                }
            }
        } catch (e: Exception) {
            Log.e("ReportsViewModel", "Error processing Excel file", e)
        }
    }


//    private fun<T> convertFile(inputStream: InputStream,type: T): List<T> {
//        inputStream.use { stream ->
//            val workbook: Workbook = XSSFWorkbook(stream)
//            val sheet: Sheet = workbook.getSheetAt(0)
//            val reports = mutableListOf<BranchReportLocalDTO>()
//            val branchInfo = listOf(
//                Branches.ALMNTAKA_ALHORA_NAME,
//                Branches.ALMNTAKA_ALHORA_SALES,
//                Branches.KHALDA_MULTI_BRAND_NAME,
//                Branches.KHALDA_MULTI_BRAND_SALES,
//                Branches.KHALDA_DONGFENG_NAME,
//                Branches.KHALDA_DONGFENG_SALES,
//                Branches.ARBED_NAME,
//                Branches.ARBED_SALES,
//                Branches.FLEET_NAME,
//                Branches.FLEET_SALES,
//            )
//            for (i in branchInfo.indices step 2) {
//                val branchNameCell: Cell = sheet.getRow(branchInfo[i].row).getCell(branchInfo[i].column)
//                val branchName: String = branchNameCell.stringCellValue
//                val totalSalesCell: Cell = sheet.getRow(branchInfo[i + 1].row).getCell(branchInfo[i + 1].column)
//                val totalSales: Double = totalSalesCell.numericCellValue
//                reports.add(BranchReportLocalDTO(branchName = branchName, branchSalesValue = totalSales))
//            }
//            return reports
//    }

//    override fun onClickUploadFileBranches(inputStream: InputStream) {
//        try {
//            val workbook: Workbook = XSSFWorkbook(inputStream)
//            val sheet: Sheet = workbook.getSheetAt(0)
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

    private suspend fun saveBranchesReportData(reports: List<BranchReportLocalDTO>) {
        branchesReportsDao.insertBranchesReports(reports)
    }

    private fun getChartsData() {
        getBranchesChartData()
        getCarsChartData()
        getBranchesCarsChartData()
    }

    override fun getBranchesChartData() {
        viewModelScope.launch {
            branchesReportsDao.getBranchesReports().collectLatest { reports ->
                val combinedReports = combineReportsByBranchName(reports)
                updateState { it.copy(branchesReports = combinedReports) }
            }
        }
    }

    override fun getCarsChartData() {
        viewModelScope.launch {
            carsReportsDao.getCarsReports().collectLatest { reports ->
                val combinedReports = combineReportsByCarName(reports)
                updateState { it.copy(carsReports = combinedReports) }
            }
        }
    }

    override fun getBranchesCarsChartData() {
        viewModelScope.launch {
            branchesCarsReportsDao.getBranchesCarsReports().collectLatest { reports ->
                val combinedReports = combineReportsByBranchAndCarName(reports)
                updateState { it.copy(branchesCarsReports = combinedReports) }
            }
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
            combinedReports.add(
                BranchReportLocalDTO(
                    branchName = branchName, branchSalesValue = totalSales
                )
            )
        }
        return combinedReports
    }

    private fun combineReportsByCarName(reports: List<CarReportLocalDTO>): List<CarReportLocalDTO> {
        val combinedReports = mutableListOf<CarReportLocalDTO>()
        val carSalesMap = mutableMapOf<String, Double>()

        reports.forEach { report ->
            val currentSalesValue = carSalesMap.getOrDefault(report.carName, 0.0)
            carSalesMap[report.carName] = currentSalesValue + report.carSalesValue
        }

        carSalesMap.forEach { (carName, totalSales) ->
            combinedReports.add(CarReportLocalDTO(carName = carName, carSalesValue = totalSales))
        }

        return combinedReports
    }

    private fun combineReportsByBranchAndCarName(reports: List<BranchCarsLocalDTO>): List<BranchCarsLocalDTO> {
        val combinedReportsMap = mutableMapOf<Pair<String, String>, Double>()

        // Iterate through reports to accumulate sales values for each unique branch-car combination
        reports.forEach { report ->
            val key = report.branchName to report.carName
            val currentSalesValue = combinedReportsMap.getOrDefault(key, 0.0)
            combinedReportsMap[key] = currentSalesValue + report.branchSalesValue
        }

        // Convert the map back to a list of BranchCarsLocalDTO
        val combinedReports = combinedReportsMap.map { (key, totalSales) ->
            BranchCarsLocalDTO(
                branchName = key.first,
                carName = key.second,
                branchSalesValue = totalSales
            )
        }

        return combinedReports
    }


    data class CellIndex(
        val row: Int, val column: Int
    )
}