package com.hk.portfolio.today_weather.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData

@Composable
fun SearchHistoryCell(
    item: SearchHistoryData,
    onClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .clickable { onClicked(item.query) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
        Text(text = item.query)
    }
}