package no.uio.ifi.in2000.team53.weatherapp.ui.home

import android.icu.util.Calendar
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.data.local.location.LocationRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi.PollenAPIRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.weathericons.WeatherIcons
import no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis.ForecastTimeStep
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetAPIRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.PollenData

enum class LoadingState {
    LOADING, LOADED, ERROR
}

/**
 * The UI state for the home screen.
 * @param forecasts The list of forecast time steps.
 * @param loadingState The loading state.
 * @param weatherInfo The weather information.
 * @param activities The user's activities.
 * @param recommendedActivities The recommended activities.
 * @param pollenData The pollen data.
 */
data class HomeUIState(
    val forecasts: List<ForecastTimeStep> = listOf(),
    val loadingState: LoadingState = LoadingState.LOADING,
    val weatherInfo: WeatherInfo = WeatherInfo(), // Placeholder for weather information
    val activities: List<Activity> = emptyList(), // Placeholder for user's activities
    val recommendedActivities: ActivityIcons  = ActivityIcons(), // Placeholder for recommended activities
    val pollenData: PollenData? = null
)

/**
 * The weather information.
 * @param info The weather information.
 */
data class WeatherInfo(
    val info : String = ""
)

/**
 * The activity.
 * @param name The name of the activity.
 */
data class Activity(
    val name : String = ""
)

/**
 * The activity icons.
 * @param sportsIcons The list of sports icons.
 */
data class ActivityIcons(
    val sportsIcons : List<Int> = listOf(
        R.drawable.sports_soccer,
        R.drawable.sport_skateboarding,
        R.drawable.sports_tennis,
        R.drawable.sports_kayaking,
        R.drawable.sports_running,
        R.drawable.sports_swimming,
        R.drawable.sports_handball,
        R.drawable.sports_sailing,
        R.drawable.sports_paragliding
    )
)


/**
 * The view model for the home screen.
 * This view model is responsible for managing the home screen state.
 *
 * @param metAPIRepository The repository for weather data.
 * @param locationRepository The repository for location data.
 * @param pollenAPIRepository The repository for pollen data.
 * @param userActivityRepository The repository for user activity data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val metAPIRepository: MetAPIRepository,
    private val locationRepository: LocationRepository,
    private val pollenAPIRepository: PollenAPIRepository,
    private val userActivityRepository: UserActivityRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState

    private val _greetingText = MutableStateFlow("")
    val greetingText: StateFlow<String> = _greetingText

    private val _userActivitiesState = MutableStateFlow<List<UserActivityEntity>>(emptyList())
    val userActivitiesState: StateFlow<List<UserActivityEntity>> = _userActivitiesState

    val weatherIcons = WeatherIcons()

    /** Initializes the view model. */
    init {
        observeUserActivities()
        loadWeatherAndPollen()
        initializeGreetingText(Calendar.getInstance())
    }

    /** Loads weather and pollen data. */
    private fun loadWeatherAndPollen() = viewModelScope.launch {
        setLoadingState()
        try {
            withContext(Dispatchers.IO) {
                val location = locationRepository.getCurrentLocation().split(",").let {
                    Location("").apply {
                        latitude = it[0].toDouble()
                        longitude = it[1].toDouble()
                    }
                }
                metAPIRepository.loadWeather(location.latitude, location.longitude)
                loadPollenDataForCurrentRegion(location)
            }
        } catch (e: Exception) {
            setErrorState(e)
        }
        updateUIState()
    }

    /** Sets the loading state. */
    private fun setLoadingState() {
        _uiState.value = HomeUIState(loadingState = LoadingState.LOADING)
    }

    /** Sets the error state.
     * @param e The exception to set.
     */
    private fun setErrorState(e: Exception) {
        Log.d("HomeViewModel", "Error loading weather: ${e.message}")
        _uiState.value = HomeUIState(loadingState = LoadingState.ERROR, forecasts = emptyList())
    }
    /** Observes user activities. */
    private fun observeUserActivities() {
        viewModelScope.launch {
            try {
                val localActivities = userActivityRepository.getAllUserActivities().first()
                _userActivitiesState.value = localActivities
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading user activities: $e")
                _userActivitiesState.value = emptyList()
            }
        }
    }

    /** Loads pollen data for the current region.
     * @param location The location to load pollen data for.
     */
    private fun loadPollenDataForCurrentRegion(location: Location) = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                pollenAPIRepository.loadPollenDataForRegion(location.latitude, location.longitude)
                setPollenDataForRegionState()
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error loading pollen data for current region: ${e.message}", e)
            _uiState.value = _uiState.value.copy(pollenData = null)
        }
    }

    /** Sets the pollen data for the region state. */
    private fun setPollenDataForRegionState() {
        try {
            val pollenData = pollenAPIRepository.getPollenData()
            _uiState.value = _uiState.value.copy(pollenData = pollenData)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error getting pollen data for region: ${e.message}", e)
            _uiState.value = _uiState.value.copy(pollenData = null)
        }
    }

    /** Initializes the greeting text.
     * @param calendar The calendar to update the greeting text with.
     */
    fun initializeGreetingText(calendar: Calendar) {
        updateGreetingText(calendar)
    }

    /** Updates the greeting text.
     * @param calendar The calendar to update the greeting text with.
     */
    private fun updateGreetingText(calendar: Calendar) {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        Log.d("CurrentHour", "Current hour: $currentHour")
        val greeting = when (currentHour) {
            in 0 until 10 -> "God morgen"
            in 10 until 12 -> "God formiddag"
            in 12 until 18 -> "God ettermiddag"
            in 18 until 24 -> "God kveld"
            else -> "God kveld"
        }
        _greetingText.value = greeting
    }

    /** Updates the UI state. */
    private fun updateUIState() {
        val cachedForecast = metAPIRepository.getForecast()
        if (_uiState.value.pollenData != null && _greetingText.value.isNotBlank()) {
            _uiState.value = HomeUIState(
                forecasts = cachedForecast,
                loadingState = LoadingState.LOADED,
                pollenData = _uiState.value.pollenData,
            )
        } else{
            _uiState.value = HomeUIState(
                forecasts = cachedForecast,
                loadingState = LoadingState.LOADED,
                pollenData = null
            )
        }
    }

}