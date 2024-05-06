package com.leithcarsreports.presentation.reports

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aay.compose.donutChart.model.PieChartData
import com.leithcarsreports.R
import com.leithcarsreports.presentation.composable.BarChartSample
import com.leithcarsreports.presentation.composable.DonutChartSample
import com.leithcarsreports.presentation.composable.EventHandler
import com.leithcarsreports.presentation.composable.PieChartSample
import com.leithcarsreports.presentation.composable.XCalenderTextField
import com.leithcarsreports.presentation.ui.theme.Purple80
import com.leithcarsreports.presentation.ui.theme.background
import com.leithcarsreports.presentation.ui.theme.onBackground87
import java.io.IOException
import java.io.InputStream

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    EventHandler(viewModel.effect) { effect ->
        when (effect) {
            is ReportsUIEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
            }
        }
    }
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
    val branchesChartData = if (state.branchesReports.isNotEmpty()) {
        state.branchesReports.mapIndexed { index, report ->
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

    val carsChartData = if (state.carsReports.isNotEmpty()) {
        state.carsReports.mapIndexed { index, report ->
            val colorIndex = index % colors.size
            PieChartData(
                partName = report.carName,
                data = report.carSalesValue,
                color = colors[colorIndex],
            )
        }
    } else {
        emptyList()
    }

    val xlsxFilter = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val branchesLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    val branchesCarsLauncher = launchOpenDocument(listener::onClickUploadFileBranchesCars)
    val staffLauncher = launchOpenDocument(listener::onClickUploadFileBranches)
    val carsLauncher = launchOpenDocument(listener::onClickUploadFileCars)
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Use the hex code for light gray
    ) {
        val imagePainter: Painter =
            painterResource(id = R.drawable.screenshot) // Replace 'your_image' with your actual image resource name
        Image(
            painter = imagePainter,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize().alpha(0.4f), // Apply 50% transparency
            contentScale = ContentScale.Fit
        )
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                modifier = Modifier,
                state = pagerState,
                userScrollEnabled = true,
            ) { page ->
                when (page) {
                    0 -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "From:",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = onBackground87,
                                        modifier = Modifier.padding(end = 18.dp)
                                    )
                                    XCalenderTextField(
                                        modifier = Modifier.width(155.dp),
                                        onClick = { listener.onChangeFromDate(it) },
                                        selectedDate = state.fromDate
                                    )
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "To:",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Center,
                                            color = onBackground87,
                                            modifier = Modifier.padding(start = 16.dp, end = 18.dp)
                                        )
                                        XCalenderTextField(
                                            modifier = Modifier.width(155.dp),
                                            onClick = { listener.onChangeToDate(it) },
                                            selectedDate = state.toDate
                                        )
                                    }
                                }
                                Button(modifier = Modifier.padding(start = 20.dp),
                                    onClick = { listener.getBranchesWithinDates() }) {
                                    Text(text = "Get", fontSize = 24.sp)
                                }
                            }
                            DonutChartSample(branchesChartData)
                        }
                    }

                    1 -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "From:",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = onBackground87,
                                        modifier = Modifier.padding(end = 18.dp)
                                    )
                                    XCalenderTextField(
                                        modifier = Modifier.width(155.dp),
                                        onClick = { listener.onChangeFromDate(it) },
                                        selectedDate = state.fromDate
                                    )
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "To:",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Center,
                                            color = onBackground87,
                                            modifier = Modifier.padding(start = 16.dp, end = 18.dp)
                                        )
                                        XCalenderTextField(
                                            modifier = Modifier.width(155.dp),
                                            onClick = { listener.onChangeToDate(it) },
                                            selectedDate = state.toDate
                                        )
                                    }
                                }
                                Button(modifier = Modifier.padding(start = 20.dp),
                                    onClick = { listener.getCarsWithinDates() }) {
                                    Text(text = "Get", fontSize = 24.sp)
                                }
                            }
                            PieChartSample(carsChartData)
                        }
                    }

                    2 -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "From:",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = onBackground87,
                                        modifier = Modifier.padding(end = 18.dp)
                                    )
                                    XCalenderTextField(
                                        modifier = Modifier.width(155.dp),
                                        onClick = { listener.onChangeFromDate(it) },
                                        selectedDate = state.fromDate
                                    )
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "To:",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Center,
                                            color = onBackground87,
                                            modifier = Modifier.padding(start = 16.dp, end = 18.dp)
                                        )
                                        XCalenderTextField(
                                            modifier = Modifier.width(155.dp),
                                            onClick = { listener.onChangeToDate(it) },
                                            selectedDate = state.toDate
                                        )
                                    }
                                }
                                Button(modifier = Modifier.padding(start = 20.dp),
                                    onClick = { listener.getBranchesCarsWithinDates() }) {
                                    Text(text = "Get", fontSize = 24.sp)
                                }
                            }
                            BarChartSample(state.branchesCarsReports)
                        }
                    }

                    else -> {

                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    when (pagerState.currentPage) {
                        0 -> {
                            branchesLauncher.launch(xlsxFilter)
                        }

                        1 -> {
                            Log.d("onClickUploadFileCars: ", "${pagerState.currentPage}")
                            carsLauncher.launch(xlsxFilter)
                        }

                        2 -> {
                            Log.d("onClickUploadFileCars: ", "${pagerState.currentPage}")
                            branchesCarsLauncher.launch(xlsxFilter)
                        }

                        else -> {

                        }
                    }
                }) {
                    Text(text = state.buttonText, fontSize = 32.sp)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    onClick = {
                        when (pagerState.currentPage) {
                            0 -> {
                                listener.getBranchesChartData()
                            }

                            1 -> {
                                listener.getCarsChartData()
                            }

                            2 -> {
                                listener.getBranchesCarsChartData()
                            }

                            else -> {
                            }
                        }
                    }) {
                    Text(text = "Get Latest", fontSize = 32.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
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