package com.leithcarsreports.presentation.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun isFromDateIsBeforeTheToDate(fromDate: String, toDate: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return try {
        val fromDateParsed = LocalDate.parse(fromDate, formatter)
        val toDateParsed = LocalDate.parse(toDate, formatter)
        fromDateParsed.isBefore(toDateParsed) || fromDateParsed.isEqual(toDateParsed)
    } catch (e: Exception) {
        false
    }
}

fun convertStringToTimestamp(dateString: String, format: String = "yyyy-MM-dd"): Long {
    if(dateString.trim().isEmpty())return 0
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = sdf.parse(dateString)
    return date?.time ?: 0
}

fun convertStringTimeToTimestamp(timeString: String, format: String = "h:mma"): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val time = sdf.parse(timeString)
    return time?.time ?: 0
}

fun convertTimestampToString(timestamp: Long, format: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}