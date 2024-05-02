package com.leithcarsreports.presentation.reports

import android.os.Build
import androidx.annotation.RequiresApi
import com.leithcarsreports.presentation.base.BaseViewModel
import com.leithcarsreports.services.FilePickerManager
import com.leithcarsreports.services.IFilePickerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val filePickerManager: IFilePickerManager
) : BaseViewModel<ReportsUIState, ReportsUIEffect>(ReportsUIState()), ReportsInteractionListener {

    override fun onClickUploadFile() {
        filePickerManager.pickExcelFile()
    }
}