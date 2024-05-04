package com.leithcarsreports.presentation.reports

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aay.compose.donutChart.model.PieChartData
import com.leithcarsreports.R
import com.leithcarsreports.presentation.composable.DonutChartSample
import java.io.IOException

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    ReportsScreenContent(state, viewModel)
}

@Composable
fun ReportsScreenContent(state: ReportsUIState, listener: ReportsInteractionListener) {
    Log.d(
        "ReportsScreenContent",
        "State for UI: ${state.reports.map { "${it.branchName}: ${it.branchSalesValue}" }}"
    )

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { resultUri ->
            resultUri?.let {
                try {
                    context.contentResolver.openInputStream(it)?.use { stream ->
                        Log.d(
                            "ReportsScreenContent",
                            "Available bytes before processing: ${stream.available()}"
                        )
                        listener.onClickUploadFile(stream)
                        val bytes = stream.readBytes()
                        Log.d("ReportsScreenContent", "Read bytes length: ${bytes.size}")
                    }
                } catch (e: IOException) {
                    Log.e(
                        "ReportsScreenContent",
                        "IOException when handling the stream: ${e.message}"
                    )
                } catch (e: Exception) {
                    Log.e(
                        "ReportsScreenContent",
                        "General exception when processing the file: ${e.message}"
                    )
                }
            }
        }
    Box(
        modifier = Modifier.fillMaxSize(). background (Color(0xFFF5F5F5)) // Use the hex code for light gray
    ) {
        val imagePainter: Painter =
            painterResource(id = R.drawable.screenshot) // Replace 'your_image' with your actual image resource name
        Image(
            painter = imagePainter,
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()

                .alpha(0.4f), // Apply 50% transparency
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    Log.d("reports", "${index},${report}")
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
            Button(onClick = { launcher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") }) {
                Text(text = state.buttonText, fontSize = 12.sp)
            }
        }
    }
}