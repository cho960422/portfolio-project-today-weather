package com.hk.portfolio.today_weather.presentation.screen.write.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel @Inject constructor(

): ViewModel() {
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
    var multiDay = mutableStateOf<Boolean>(false)
        private set

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
}