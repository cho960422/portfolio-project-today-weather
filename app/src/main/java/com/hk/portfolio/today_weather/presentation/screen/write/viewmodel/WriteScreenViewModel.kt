package com.hk.portfolio.today_weather.presentation.screen.write.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.core.UiState
import com.hk.portfolio.today_weather.core.util.AlarmManagerUtil
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.domain.usecase.event.DeleteEventUseCase
import com.hk.portfolio.today_weather.domain.usecase.event.GetEventUseCase
import com.hk.portfolio.today_weather.domain.usecase.event.InsertEventUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.DeleteWeatherByEventIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel @Inject constructor(
    private val insertEventUseCase: InsertEventUseCase,
    private val getEventUseCase: GetEventUseCase,
    private val deleteWeatherByEventIdUseCase: DeleteWeatherByEventIdUseCase,
    private val application: Application
): AndroidViewModel(application = application) {
    val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    var name = mutableStateOf("")
        private set
    var startDate = mutableStateOf<LocalDate?>(null)
        private set
    var endDate = mutableStateOf<LocalDate?>(null)
        private set
    var place = mutableStateOf<PlaceEntity?>(null)
        private set
    var alarm = mutableStateOf<LocalTime?>(null)
        private set
    var id = mutableStateOf<String?>(null)
        private set
    var notificationId = mutableStateOf<Int?>(null)
        private set
    var multiDay = mutableStateOf(false)
        private set
    var submitState = mutableStateOf<UiState<Boolean>>(UiState())
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExistEvent(inputId: String, onNext:(PlaceEntity) -> Unit) {
        if (id.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val eventEntity = getEventUseCase(inputId)
                id.value = eventEntity.id
                onChangeStartDate(eventEntity.startDate)
                onChangeEndDate(eventEntity.endDate)
                onChangeAlarm(eventEntity.alarm?.toLocalTime())
                if (eventEntity.endDate != null) {
                    onChangeMultiDay()
                }
                onChangeName(eventEntity.eventName)
                onChangePlace(eventEntity.place)
                onNext(eventEntity.place)
            }
        }
    }

    fun onChangeName(s: String) {
        changeName(s)
    }

    private fun changeName(s: String) {
        name.value = s
    }

    fun onChangeStartDate(date: LocalDate?) {
        changeStartDate(date)
    }

    private fun changeStartDate(date: LocalDate?) {
        startDate.value = date
    }

    fun onChangeEndDate(date: LocalDate?) {
        changeEndDate(date)
    }

    private fun changeEndDate(date: LocalDate?) {
        endDate.value = date
    }

    fun onChangePlace(placeEntity: PlaceEntity?) {
        changePlace(placeEntity)
    }

    private fun changePlace(placeEntity: PlaceEntity?) {
        place.value = placeEntity
    }

    fun onChangeAlarm(dateTime: LocalTime?) {
        changeAlarm(dateTime)
    }

    private fun changeAlarm(dateTime: LocalTime?) {
        alarm.value = dateTime
    }

    fun onChangeMultiDay() {
        changeMultiDay()
    }

    private fun changeMultiDay() {
        val reverse = !multiDay.value
        multiDay.value = reverse
        if (!reverse) {
            endDate.value = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submit() {
        val alarmDate = if (alarm.value != null)
            startDate.value!!.atStartOfDay().withHour(alarm.value!!.hour).withMinute(alarm.value!!.minute)
        else null
        val mNotificationId: Int? = if (id.value == null && alarmDate != null && notificationId.value == null) {
            AlarmManagerUtil.makeNotificationId(alarmDate)
        } else notificationId.value
        val alarmManager: AlarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val request = EventEntity(
            id = id.value?: "${startDate.value}-submit_at_${LocalDateTime.now()}",
            eventName = name.value,
            startDate = startDate.value!!,
            endDate = endDate.value,
            place = place.value!!,
            alarm = alarmDate,
            updateAt = LocalDateTime.now(),
            broadcastId = mNotificationId
        )

        if (startDate.value!! >= LocalDate.now() && id.value?.isNotEmpty() == true) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteWeatherByEventIdUseCase(id.value!!)
            }
        }
        if (alarmDate == null) {
            if (notificationId.value != null) AlarmManagerUtil.cancelPushAlarmSchedule(application, mNotificationId?: 0, alarmManager)
        } else {
            AlarmManagerUtil.makePushAlarmSchedule(application, mNotificationId?: 0, request, alarmDate, alarmManager)
        }
        viewModelScope.launch(Dispatchers.IO) {
            insertEventUseCase(request).onEach {
                when (it) {
                    is JobState.Loading -> {
                        submitState.value = UiState(isLoading = true)
                    }
                    is JobState.Success -> {
                        submitState.value = UiState(data = true)
                    }
                    is JobState.Error -> {
                        submitState.value = UiState(isError = true, message = it.message?:"등록되지 않았습니다.")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun confirmErrorMsg() {
        submitState.value = UiState()
    }
}