package com.leithcarsreports.presentation.composable

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leithcarsreports.R
import com.leithcarsreports.presentation.ui.theme.Border
import com.leithcarsreports.presentation.ui.theme.CardColor
import com.leithcarsreports.presentation.ui.theme.PrimaryColor
import com.leithcarsreports.presentation.ui.theme.onBackground60
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun XCalenderTextField(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: String = "",
    borderColor: Color = CardColor,
    containerColor: Color = CardColor,
    notAllowToPickPastDates: Boolean = false,
    enabled: Boolean = true,
    hasError: Boolean = false,
    errorMessage: String = ""
) {
    val selectableDates =
        if (notAllowToPickPastDates) PastOrPresentSelectableDates else DatePickerDefaults.AllDates
    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates,
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var showDialog by remember { mutableStateOf(false) }
    val selectDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    if (showDialog && enabled) {
        DatePickerDialog(
            modifier = Modifier,
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        onClick(selectDate)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)

                ) {
                    Text(text = "Done")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Border,
                    )
                ) {
                    Text(text = "Cancel", color = onBackground60)
                }
            },
            tonalElevation = 0.dp,
            colors = DatePickerDefaults.colors(
                containerColor = CardColor,
                selectedDayContainerColor = PrimaryColor
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = CardColor,
                    selectedDayContainerColor = PrimaryColor
                ),
            )
        }
    }
    Column {
        val cardModifier = if (enabled) {
            modifier
                .height(56.dp)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable { showDialog = true }
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp)
        } else {
            modifier
                .height(56.dp)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp)
                .padding(start = 8.dp)
        }
        Row(
            modifier = cardModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = selectedDate.ifEmpty { "Select Date" },
                style = MaterialTheme.typography.labelMedium,
                color = onBackground60
            )
            if (enabled) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    tint = onBackground60
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private object PastOrPresentSelectableDates : SelectableDates {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val startOfDay = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        return utcTimeMillis >= startOfDay
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
private fun convertMillisToDate(millis: Long): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.format(Date(millis))
    } catch (e: Exception) {
        ""
    }
}