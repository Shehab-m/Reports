package com.leithcarsreports.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters

@Composable
fun BarChartSample(data: List<BarParameters>, xAxisData: List<String>) {
    Box(
        Modifier.fillMaxWidth().height(650.dp).padding(horizontal = 120.dp)
    ) {
//        BarChart(
//            chartParameters = data,
//            gridColor = Color.LightGray,
//            isShowGrid = true,
//            xAxisData = xAxisData,
//            animateChart = true,
//            showGridWithSpacer = true,
//            yAxisStyle = TextStyle(
//                fontSize = 14.sp,
//                color = Color.DarkGray,
//            ),
//            xAxisStyle = TextStyle(
//                fontSize = 14.sp,
//                color = Color.DarkGray,
//                fontWeight = FontWeight.W400
//            ),
//            yAxisRange = 15,
//            barWidth = 16.dp
//        )
        val testBarParameters: List<BarParameters> = listOf(
            BarParameters(
                dataName = "Services 1",
                data = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0),
                barColor = Color(0xFF0027FF)
            ),
            BarParameters(
                dataName = "Services 2",
                data = listOf(20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0),
                barColor = Color(0xFF3F51B5),
            ),
            BarParameters(
                dataName = "Services 3",
                data = listOf(30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0),
                barColor = Color(0xFF1F233A),
            ),
            BarParameters(
                dataName = "Services 4",
                data = listOf(40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0),
                barColor = Color(0xFFADC9FF),
            ),
        )
        BarChart(
            chartParameters = testBarParameters,
            gridColor = Color.LightGray,
            xAxisData = listOf("2017", "2018", "2019", "2020", "2021", "2022", "2023"),
            isShowGrid = true,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 15,
            barWidth = 16.dp
        )
    }
}