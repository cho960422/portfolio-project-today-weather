package com.hk.portfolio.today_weather.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDate: LocalDate?,
    disableDate: LocalDate?=null,
    onDismiss: () -> Unit,
    confirmButtonClicked: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {
        // 공부하기
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    LaunchedEffect(Unit) {
        if (selectedDate != null) {
            val start =
                ZonedDateTime.of(selectedDate.atTime(12, 0), ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()

            datePickerState.setSelection(start)
        }
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    confirmButtonClicked(
                        convertLongToLocalDate(datePickerState.selectedDateMillis!!),
                    )
                },
                enabled = confirmEnabled.value
            ) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "닫기")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = {
                if (disableDate != null) {
                    convertLocalDateToLong(disableDate) < it
                } else true
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertLocalDateToLong(date: LocalDate): Long {
    return ZonedDateTime.of(date.atTime(0,1, 1), ZoneId.systemDefault()).toInstant()
        .toEpochMilli()
}

// 공부하기
@RequiresApi(Build.VERSION_CODES.O)
fun convertLongToLocalDate(time: Long): LocalDate {
    val instant = Instant.ofEpochMilli(time)
    val zoneId = ZoneId.systemDefault() // Use the system default time zone
    return instant.atZone(zoneId).toLocalDate()
}