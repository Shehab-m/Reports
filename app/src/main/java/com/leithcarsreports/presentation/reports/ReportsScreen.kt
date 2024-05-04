package com.leithcarsreports.presentation.reports

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aay.compose.donutChart.model.PieChartData
import com.leithcarsreports.presentation.composable.DonutChartSample
import java.io.IOException
import java.io.InputStream

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    ReportsScreenContent(state, viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReportsScreenContent(state: ReportsUIState, listener: ReportsInteractionListener) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
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
    val xlsxFilter = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val branchesLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    val branchesCarsLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    val staffLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    val carsLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            userScrollEnabled = true,
        ) { page ->

            when (page) {
                1 -> {
                    DonutChartSample(chartData)
                }

                2 -> {

                }

                else -> {

                }
            }
        }
        Button(
            onClick = { when (pagerState.currentPage) {
                1 -> {
                    branchesLauncher.launch(xlsxFilter)
                }
            }
        }
        ) {
            Text(text = state.buttonText, fontSize = 42.sp)
        }
    }
}

@Composable
fun launchOpenDocument(function: (stream: InputStream) -> Unit = {}): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { resultUri ->
        resultUri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    // Debugging: Log the available bytes (Remove in production!)
                    Log.d(
                        "ReportsScreenContent",
                        "Available bytes before processing: ${stream.available()}"
                    )

                    function(stream)

                    // Optionally read the stream here if needed
                    val bytes = stream.readBytes()  // Only if necessary for debugging or processing
                    Log.d("ReportsScreenContent", "Read bytes length: ${bytes.size}")
                }
            } catch (e: IOException) {
                Log.e(
                    "ReportsScreenContent", "IOException when handling the stream: ${e.message}"
                )
            } catch (e: Exception) {
                Log.e(
                    "ReportsScreenContent",
                    "General exception when processing the file: ${e.message}"
                )
            }
        }
    }
}