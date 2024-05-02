package com.leithcarsreports.presentation.reports

import android.util.Log
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
import java.io.IOException

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    ReportsScreenContent(state, viewModel)
}
@Composable
fun ReportsScreenContent(state: ReportsUIState, listener: ReportsInteractionListener) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { resultUri ->
        resultUri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    Log.d("ReportsScreenContent", "Available bytes before processing: ${stream.available()}")
                    listener.onClickUploadFile(stream)
                    val bytes = stream.readBytes()
                    Log.d("ReportsScreenContent", "Read bytes length: ${bytes.size}")
                }
            } catch (e: IOException) {
                Log.e("ReportsScreenContent", "IOException when handling the stream: ${e.message}")
            } catch (e: Exception) {
                Log.e("ReportsScreenContent", "General exception when processing the file: ${e.message}")
            }
        }
    }

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

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { listener.toggleChartDisplay() }) {
            Text("Toggle Chart", fontSize = 18.sp)
        }

        if (state.chartToggle) {
            DonutChartSample(chartData)
        } else {
            // Example of how you might want to modify the chart for the second state
            DonutChartSample(chartData.map { it.copy(data = it.data * 1.1) })  // Example modification
        }

        Button(onClick = { launcher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") }) {
            Text(text = state.buttonText, fontSize = 42.sp)
        }
    }
}