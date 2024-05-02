package com.leithcarsreports.presentation.reports

import com.leithcarsreports.presentation.base.BaseInteractionListener
import java.io.InputStream

interface ReportsInteractionListener : BaseInteractionListener {
    fun onClickUploadFile(inputStream: InputStream)
}