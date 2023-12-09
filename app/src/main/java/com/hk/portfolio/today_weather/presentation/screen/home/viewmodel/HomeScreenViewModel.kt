package com.hk.portfolio.today_weather.presentation.screen.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.UiState
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.core.util.WeatherUtil.TO_GPS
import com.hk.portfolio.today_weather.dataStore
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.usecase.event.GetAllEventListUseCase
import com.hk.portfolio.today_weather.domain.usecase.event.GetTodayEventUseCase
import com.hk.portfolio.today_weather.domain.usecase.tour.GetTourListUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.GetCurrentWeatherUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.GetWeatherUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.WriteWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getAllEventListUseCase: GetAllEventListUseCase,
    private val writeWeatherUseCase: WriteWeatherUseCase,
    private val getTodayEventUseCase: GetTodayEventUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getTourListUseCase: GetTourListUseCase,
    private val application: Application
): AndroidViewModel(application){
    var isUpdating = mutableStateOf(false)
        private set
    val todayEventList = getTodayEventUseCase(Unit).stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)
//    val locationCallback = object : LocationCallback() {
//        @RequiresApi(Build.VERSION_CODES.O)
//        override fun onLocationResult(locationResult: LocationResult) {
//            val location = locationResult.locations.last()
//            viewModelScope.launch {
//            }
//            Log.d("current Location :: ", "${location.latitude}, ${location.longitude}")
//        }
//    }
    var currentWeather = mutableStateOf<UiState<WeatherConditionEntity>>(UiState(isLoading = true))
        private set
    val locationClient = LocationServices.getFusedLocationProviderClient(application)
    var tourList: Flow<PagingData<TourEntity>>? = null
        private set
    private val CATEGORY_KEY = intPreferencesKey("category")
    val category: Flow<TourContentTypeEnum?> = application.baseContext.dataStore.data.map { preferences ->
        TourContentTypeEnum.findByCode(preferences[CATEGORY_KEY])
    }
//    var category = mutableStateOf<TourContentTypeEnum?>(null)
//        private set

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val requestAvailableTime: List<Pair<LocalTime, LocalTime>> =
            listOf(
                Pair(toLocalTime("0200"), toLocalTime("0210")),
                Pair(toLocalTime("0500"), toLocalTime("0510")),
                Pair(toLocalTime("0800"), toLocalTime("0810")),
                Pair(toLocalTime("1100"), toLocalTime("1110")),
                Pair(toLocalTime("1400"), toLocalTime("1410")),
                Pair(toLocalTime("1700"), toLocalTime("1710")),
                Pair(toLocalTime("2000"), toLocalTime("2010")),
                Pair(toLocalTime("2300"), toLocalTime("2310")),
            )

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalTime(time: String): LocalTime {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkAndUpdateWeather() {
        isUpdating.value = true
        delay(1000)
        viewModelScope.launch {
            getCurrentWeather()
        }
        val baseDateTime = searchAvailableTime(LocalDateTime.now())
        val needUpdateEventList = filterNeedUpdateEvent(baseDateTime)
        updateWeather(needUpdateEventList, baseDateTime)
        isUpdating.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateWeather(list: List<EventAndWeatherEntity>, baseDateTime: LocalDateTime) {
        val now = LocalDate.now()
        list.forEach {
            val weatherData = getWeatherUseCase(
                with(it.eventEntity) {
                    GetWeatherUseCase.Request(
                        eventId = id,
                        nx = place.nx,
                        ny = place.ny,
                        baseDateTime = baseDateTime,
                        count = 100,
                        date = if (startDate < now) startDate else now
                    )
                }
            )
            weatherData?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    writeWeatherUseCase(it)
                }
            }
            delay(500)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun filterNeedUpdateEvent(todayNeedUpdateTime: LocalDateTime): List<EventAndWeatherEntity> {
        val now = LocalDateTime.now()
        return getAllEventListUseCase(now.toLocalDate()).filter {
            if (it.weatherEntity?.baseDateTime == null) true
            else
                it.weatherEntity.baseDateTime < todayNeedUpdateTime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchAvailableTime(now: LocalDateTime): LocalDateTime {
        if (now.toLocalTime() < LocalTime.of(2, 11)) return now.minusDays(1).withHour(
            requestAvailableTime.last().first.hour
        ).withMinute(0)
        val nowTime = now.toLocalTime()
        val timePair = requestAvailableTime.last {
            it.second < nowTime
        }

        return now.withHour(timePair.first.hour).withMinute(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentWeather() {
        startLocationUpdate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        viewModelScope.launch (Dispatchers.IO) {
            locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    viewModelScope.launch {
                        updateCurrentWeather(
                            location.latitude,
                            location.longitude
                        )
                    }
                } else {
                    Log.d("location :: ", "Location is NULL")
                    startLocationUpdate()
                }
            }.addOnFailureListener {
                it.printStackTrace()
                startLocationUpdate()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateCurrentWeather(
        nx: Double,
        ny: Double
    ) {
        getCurrentWeatherUseCase(
            GetCurrentWeatherUseCase.Request(
                nx = nx,
                ny = ny,
                dateTime = LocalDateTime.now()
            )
        ).onEach {
            when (it) {
                is JobState.Loading -> {
                    currentWeather.value = currentWeather.value.copy(
                        isLoading = true
                    )
                }
                is JobState.Success -> {
                    currentWeather.value = currentWeather.value.copy(
                        isLoading = false,
                        data = it.data
                    )
                }

                is JobState.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getTourList(event: EventAndWeatherEntity, category: TourContentTypeEnum?) {
        tourList = null
        val latlng = WeatherUtil.convertGRID_GPS(TO_GPS, event.eventEntity.place.nx, event.eventEntity.place.ny)
        tourList = Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            initialKey = 1,
            pagingSourceFactory = {
                getTourListUseCase(Pair(latlng, if (category == null || category == TourContentTypeEnum.Unknown) null else category))
            }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    fun selectTourCategory(inputCategory: TourContentTypeEnum?) {
        viewModelScope.launch {
            application.baseContext.dataStore.edit { settings ->
                settings[CATEGORY_KEY] = inputCategory?.category ?: TourContentTypeEnum.Unknown.category
            }
        }
    }
}