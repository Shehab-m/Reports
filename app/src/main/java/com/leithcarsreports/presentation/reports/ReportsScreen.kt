package com.leithcarsreports.presentation.reports

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aay.compose.donutChart.model.PieChartData
import com.leithcarsreports.presentation.composable.DonutChartSample

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    ReportsScreenContent(state, viewModel)
}

@Composable
fun ReportsScreenContent(state: ReportsUIState, listener: ReportsInteractionListener) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
            result?.let {
                val inputStream = context.contentResolver.openInputStream(result)
                inputStream?.let {
                    listener.onClickUploadFile(inputStream)
                }
                val bytes = inputStream?.readBytes()
                println(bytes)
                inputStream?.close()
            }
        }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        val colors = listOf(
            Color(0xFFADC9FF),
            Color(0xFF0027FF),
            Color(0xFF1F233A),
            Color(0xFFFF0000),
            Color(0xFF00FF00),
            Color(0xFFFFFF00),
        )
        val chartData = if (state.reports.isNotEmpty()) {
            state.reports.mapIndexed { index, report ->
                val colorIndex = index % colors.size
                PieChartData(
                    partName = report.branchName,
                    data = report.branchSalesValue,
                    color = colors[colorIndex],
                )
            }
        } else {
            emptyList()
        }

        DonutChartSample(chartData)
        Button(onClick = {
            launcher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }) {
            Text(text = state.buttonText, fontSize = 42.sp)
        }
    }
}