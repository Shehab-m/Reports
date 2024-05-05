package com.leithcarsreports.presentation.reports

import com.leithcarsreports.presentation.base.BaseInteractionListener
import java.io.InputStream

interface ReportsInteractionListener : BaseInteractionListener {
    fun onClickUploadFileBranches(inputStream: InputStream)
    fun onClickUploadFileCars(inputStream: InputStream)
    fun onClickUploadFileBranchesCars(inputStream: InputStream)
}