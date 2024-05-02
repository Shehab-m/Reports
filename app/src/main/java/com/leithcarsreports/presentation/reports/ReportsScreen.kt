package com.leithcarsreports.presentation.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leithcarsreports.presentation.composable.DonutChartSample

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    ReportsScreenContent(state, viewModel)
}

@Composable
fun ReportsScreenContent(state: ReportsUIState, listener: ReportsInteractionListener) {

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        DonutChartSample()
        Button(onClick = { listener.onClickUploadFile() }) {
            Text(text = "hellloooo", fontSize = 42.sp)
        }
    }
}