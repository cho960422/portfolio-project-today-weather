package com.hk.portfolio.today_weather.ui.component

import androidx.compose.material3.AssistChip
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun CustomAssistChip(
    onClicked: (() -> Unit)?,
    label: String
) {
    AssistChip(
        onClick = {
            onClicked?.let {
                it()
            }
        },
        label = {
            Text(text = label)
        }
    )
}