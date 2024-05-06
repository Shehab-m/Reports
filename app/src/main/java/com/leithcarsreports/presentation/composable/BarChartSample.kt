package com.leithcarsreports.presentation.composable

import android.util.Log
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
import com.leithcarsreports.data.database.dto.BranchCarsLocalDTO
import com.leithcarsreports.presentation.reports.KhaldaMulitBrand

@Composable
fun BarChartSample(data: List<BranchCarsLocalDTO>) {
    val almantekaAlhoraBranch = data.filter { it.branchName == "فرع - المنطقة الحره" }
    val khaldaMulitBrandBranch = data.filter { it.branchName == "فرع - خلدا ملتي براند" }
    val arbedBranch = data.filter { it.branchName == "فرع - اربد" }
    val fleetBranch = data.filter { it.branchName == "مبيعات االسطول - Fleet" }
    val branchesNames =
        listOf("فرع - المنطقة الحره", "فرع - خلدا ملتي براند", "فرع - اربد", "Fleet")
    var vwDatatList =
        data.filter { it.carName == "VW ID 3,4,6" }.map { it.branchSalesValue }.toMutableList()
    Log.d("BarChartSample:fffffffffffff ", "${vwDatatList}")
    vwDatatList.add(0.0)
    vwDatatList.add(0.0)
    Log.d("BarChartSample:fffffffffffff ", "${vwDatatList}")
    Log.d(
        "BarChartSample:ffffffffffffffff ",
        "${data.filter { it.carName == "Mg" }.map { it.branchSalesValue }.take(4)}"
    )
    val chartData = listOf(
        BarParameters(
            dataName = "Mg",
            data = data.filter { it.carName == "Mg" }.map { it.branchSalesValue }.take(4),
            barColor = Color(0xFF0027FF)
        ),
        BarParameters(
            dataName = "JAC",
            data = data.filter { it.carName == "JAC" }.map { it.branchSalesValue }.take(4),
            barColor = Color(0xFFADC9FF)
        ),
        BarParameters(
            dataName = "JMEV",
            data = data.filter { it.carName == "JMEV" }.map { it.branchSalesValue }.take(4),
            barColor = Color(0xFF1F233A)
        ),
        BarParameters(
            dataName = "WULING",
            data = data.filter { it.carName == "WULING" }.map { it.branchSalesValue }.take(4),
            barColor = Color(0xFFFF0000)
        ),
        BarParameters(
            dataName = "Used",
            data = data.filter { it.carName == "Used" }.map { it.branchSalesValue }.take(4),
            barColor = Color(0xFFFF0000)
        ),
        BarParameters(
            dataName = "HONDA E:NS1",
            data = data.filter { it.carName.replace("\\s".toRegex(), "") == "HONDAE:NS1" }
                .map { it.branchSalesValue }.take(4),
            barColor = Color(0xFF00FF00)
        ),
        BarParameters(
            dataName = "VW ID",
            data = vwDatatList.take(4),
            barColor = Color(0xFFFFFF00)
        ),
        BarParameters(
            dataName = "BYD",
            data = data.filter { it.carName == "BYD" }.map { it.branchSalesValue },
            barColor = Color(0xF9096979)
        ),
        BarParameters(
            dataName = "DONGFENG",
            data = data.filter { it.carName == "DONGFENG" }.map { it.branchSalesValue },
            barColor = Color(0xFF9027FF)
        ),
    )
    Log.d("BarChartSample:hhtrhtr ", "${chartData}")
    Log.d("BarChartSample:hhtrhtr ", "${data.map { it.branchName }}")
//    val barChartData = if (data.isNotEmpty()) {
//        val colors = listOf(
//            Color(0xFFADC9FF),
//            Color(0xFF9027FF),
//            Color(0xFF1F233A),
//            Color(0xF9096979),
//            Color(0xFF00FF00),
//            Color(0x0F7FF530),
//        )
//        data.mapIndexed { index, report ->
//            val colorIndex = index % colors.size
//            BarParameters(
//                dataName = "Services 1",
//                data = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0),
//                barColor = Color(0xFF0027FF)
//            )
//        }
//    } else {
//        emptyList()
//    }
    Box(
        Modifier.fillMaxWidth().height(650.dp).padding(horizontal = 120.dp)
    ) {
        BarChart(
            chartParameters = if (data.isEmpty()) emptyList() else chartData.take(9),
            gridColor = Color.LightGray,
            isShowGrid = true,
            xAxisData = if (data.isEmpty()) emptyList() else branchesNames.take(4),
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
//        val testBarParameters: List<BarParameters> = listOf(
//            BarParameters(
//                dataName = "Services 1",
//                data = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0),
//                barColor = Color(0xFF0027FF)
//            ),
//            BarParameters(
//                dataName = "Services 2",
//                data = listOf(20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0),
//                barColor = Color(0xFF3F51B5),
//            ),
//            BarParameters(
//                dataName = "Services 3",
//                data = listOf(30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0),
//                barColor = Color(0xFF1F233A),
//            ),
//            BarParameters(
//                dataName = "Services 4",
//                data = listOf(40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0),
//                barColor = Color(0xFFADC9FF),
//            ),
//        )
//        BarChart(
//            chartParameters = testBarParameters,
//            gridColor = Color.LightGray,
//            xAxisData = listOf("2017", "2018", "2019", "2020", "2021", "2022", "2023"),
//            isShowGrid = true,
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
    }
}