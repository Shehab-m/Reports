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
fun DonutChartSample() {
    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "Booked Rooms",
            data = 500.0,
            color = Color(0xFF0027FF),
        ),
        PieChartData(
            partName = "Available Rooms",
            data = 700.0,
            color = Color(0xFF1F233A),
        ),
        PieChartData(
            partName = "Reserved",
            data = 500.0,
            color = Color(0xFFADC9FF),
        )
    )
    DonutChart(
        modifier = Modifier
            .width(464.dp)
            .height(517.dp),
        pieChartData = testPieChartData,
    )
}