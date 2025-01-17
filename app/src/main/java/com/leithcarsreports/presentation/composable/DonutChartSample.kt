package com.leithcarsreports.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
@Composable
fun DonutChartSample(data: List<PieChartData>) {
    DonutChart(
        modifier = Modifier
    .fillMaxWidth()
        .height(617.dp).padding(horizontal = 120.dp),
        pieChartData = data,
    )
//    PieChart(
//        modifier = Modifier
//            .width(764.dp)
//            .height(517.dp),
//        pieChartData = data,
//    )
}

