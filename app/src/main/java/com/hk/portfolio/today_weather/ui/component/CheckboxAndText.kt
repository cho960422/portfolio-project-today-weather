package com.hk.portfolio.today_weather.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hk.portfolio.today_weather.core.dpToSp

@Composable
fun CheckboxAndText(
    text:String,
    check: Boolean,
    onClicked: ()->Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .padding(end = 10.dp)
            .clickable {
                onClicked()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.size(30.dp),
            checked = check,
            onCheckedChange = {
                onClicked()
            }
        )
        Text(
            text = text,
            fontSize = 13.dp.dpToSp()
        )
    }
}