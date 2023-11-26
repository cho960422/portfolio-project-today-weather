package com.hk.portfolio.today_weather.presentation.screen.write

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hk.portfolio.today_weather.core.dpToSp
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.presentation.screen.write.viewmodel.WriteScreenViewModel
import com.hk.portfolio.today_weather.ui.component.CheckboxAndText
import com.hk.portfolio.today_weather.ui.component.CustomAssistChip
import com.hk.portfolio.today_weather.ui.component.CustomDatePicker
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen(
    isNew: Boolean,
    initPlace: String?,
    onSearchButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val viewModel = hiltViewModel<WriteScreenViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val openStartDatePicker = rememberSaveable { mutableStateOf(false) }
    val openEndDatePicker = rememberSaveable { mutableStateOf<LocalDate?>(null) }
    val openErrorTextDialog = rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val searchBarEnable = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "방문지역 등록") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBackButtonClicked() },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBackButtonClicked() },
                        text = "등록"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        val name = viewModel.name.value
        val startDate = viewModel.startDate.value
        val endDate = viewModel.endDate.value
        val place = viewModel.place.value
        val alarm = viewModel.alarm.value
        val multiDay = viewModel.multiDay.value
        val searchText = remember { mutableStateOf("") }

        val startDateStr = if (startDate != null) {
            val startDateStr =
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startDate)
            startDateStr
        } else {
            if (multiDay) {
                "시작 날짜를 선택해주세요."
            } else "일정이 예정된 날짜를 선택해주세요."
        }

        if (openStartDatePicker.value || openEndDatePicker.value != null) {
            val isStartDate: Boolean = openStartDatePicker.value
            val currentValue: LocalDate? = if (openStartDatePicker.value) {
                startDate
            } else {
                endDate
            }
            CustomDatePicker(
                selectedDate = currentValue,
                disableDate = openEndDatePicker.value,
                onDismiss = {
                    if (isStartDate) openStartDatePicker.value = false
                    else openEndDatePicker.value = null
                },
                confirmButtonClicked = { selectedDate ->
                    if (isStartDate) {
                        openStartDatePicker.value = false
                        viewModel.onChangeStartDate(selectedDate)
                    } else {
                        openEndDatePicker.value = null
                        viewModel.onChangeEndDate(selectedDate)
                    }
                }
            )
        }
        if (openErrorTextDialog.value != null) {
            AlertDialog(
                onDismissRequest = { openErrorTextDialog.value = null },

                ) {

            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    viewModel.onChangeName(it)
                },
                placeholder = {
                    Text(text = "입력해 주세요")
                },
                supportingText = {
                    Text(text = "일정 이름을 입력해주세요.")
                },
                label = {
                    Text(text = "일정 이름")
                },
                trailingIcon = {
                    if (name.isNotEmpty())
                        Icon(
                            modifier = Modifier.clickable { viewModel.onChangeName("") },
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomAssistChip(
                onClicked = {
                    openStartDatePicker.value = true
                },
                label = startDateStr
            )
            CheckboxAndText(
                onClicked = {
                    viewModel.onChangeMultiDay()
                },
                check = multiDay,
                text = "여러 날에 걸친 일정입니다."
            )
            if (multiDay) {
                val dateStr = if (endDate != null) {
                    val endDateStr =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endDate)
                    endDateStr
                } else "마지막 날을 선택해주세요."

                CustomAssistChip(
                    onClicked = {
                        if (startDate == null) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "시작 날짜를 먼저 설정해주세요.",
                                    actionLabel = "설정",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Indefinite
                                )

                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        openStartDatePicker.value = true
                                    }

                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        } else {
                            openEndDatePicker.value = startDate
                        }
                    },
                    label = dateStr
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "주소")
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "장소 이름 : $initPlace")
            Text(text = "장소 상세정보")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onSearchButtonClicked()
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "장소검색")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray
            )

            CheckboxAndText(
                onClicked = {
                    viewModel.onChangeMultiDay()
                },
                check = multiDay,
                text = "일정 당일에 알림을 받겠습니다."
            )
        }
    }
}