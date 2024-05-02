package com.leithcarsreports.presentation.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
@Composable
fun DonutChartSample(data: List<PieChartData>) {
    DonutChart(
        modifier = Modifier
            .width(464.dp)
            .height(517.dp),
        pieChartData = data,
    )
}