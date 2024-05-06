package com.leithcarsreports.presentation.reports

import com.leithcarsreports.presentation.base.BaseInteractionListener
import java.io.InputStream

interface ReportsInteractionListener : BaseInteractionListener {
    fun onClickUploadFileBranches(inputStream: InputStream)
    fun onClickUploadFileCars(inputStream: InputStream)
    fun onClickUploadFileBranchesCars(inputStream: InputStream)
    fun  onChangeToDate(date: String)
    fun onChangeFromDate(date: String)
    fun getBranchesWithinDates()
    fun getCarsWithinDates()
    fun getBranchesCarsWithinDates()
    fun getBranchesChartData()
    fun getCarsChartData()
    fun getBranchesCarsChartData()
}