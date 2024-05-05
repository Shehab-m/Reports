package com.leithcarsreports.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType

@Composable
fun LineChartSample() {
    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "",
            data = listOf(25.0, 40.0, 110.0, 140.0, 60.0, 85.0),
            lineColor = Color(0xFF0047FF),
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        )
    )

    Box(Modifier.fillMaxSize()) {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Color.Blue,
            xAxisData = listOf(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
            ),
            animateChart = true,
            showGridWithSpacer = true,
            yAxisRange = 14,
            oneLineChart = false,
            gridOrientation = GridOrientation.VERTICAL
        )
    }
}