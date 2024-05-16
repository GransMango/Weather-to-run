package no.uio.ifi.in2000.team53.weatherapp.ui.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.location.LocationRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetAPIRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis.ForecastTimeStep
import javax.inject.Inject

/**
 * The ViewModel for the daily overview screen.
 * This ViewModel is responsible for recommending activities based on the weather.
 *
 * @param locationRepository The repository for location data.
 * @param userActivityRepository The repository for user activity data.
 * @param metAPIRepository The repository for weather data.
 */
@HiltViewModel
class DailyOverviewViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userActivityRepository: UserActivityRepository,
    private val metAPIRepository: MetAPIRepository
) : ViewModel() {

    private val _recommendedActivities = MutableStateFlow<List<UserActivityEntity>>(emptyList())
    val recommendedActivities: StateFlow<List<UserActivityEntity>> = _recommendedActivities

    init {
        recommendActivitiesBasedOnWeather()
    }

    /**
     * Recommends activities based on the weather.
     * This function fetches the current location and weather data, and filters the user's activities based on the weather.
     */
    private fun recommendActivitiesBasedOnWeather() = viewModelScope.launch {
        val location = getCurrentLocation()
        try {
            metAPIRepository.loadWeather(location.latitude, location.longitude)
            val weather = metAPIRepository.getForecast().first()
            val activities = userActivityRepository.getAllUserActivities().first()
            val suitableActivities = activities.filter { activity ->
                isActivitySuitable(activity, weather)
            }.sortedByDescending { it.selected }
            _recommendedActivities.value = suitableActivities.take(5) // Taking the top 5 activities

        } catch (e: Exception) {
            _recommendedActivities.value = emptyList()
        }



    }

    /**
     * Fetches the current location.
     * @return The current location.
     */
    private suspend fun getCurrentLocation(): Location {
        val locString = locationRepository.getCurrentLocation().split(",")
        return Location("").apply {
            latitude = locString[0].toDouble()
            longitude = locString[1].toDouble()
        }
    }

    /**
     * Checks if an activity is suitable based on the weather.
     * @param activity The activity to check.
     * @param weather The weather data.
     * @return True if the activity is suitable, false otherwise.
     */
    private fun isActivitySuitable(activity: UserActivityEntity, weather: ForecastTimeStep): Boolean {
        val conditions = weather.data.instant?.details
        return conditions?.let {
            airTemperatureCheck(activity, it.airTemperature) &&
                    precipitationCheck(activity, weather) &&
                    windSpeedCheck(activity, it.windSpeed, it.windSpeedOfGust)
        } ?: true // Return true if weather details are null
    }

    /**
     * Checks if the precipitation is suitable for an activity.
     * @param activity The activity to check.
     * @param weather The weather data.
     * @return True if the precipitation is suitable, false otherwise.
     */
    private fun precipitationCheck(activity: UserActivityEntity, weather: ForecastTimeStep): Boolean {
        val avgPrecipitation = getAveragePrecipitation(weather)
        return if (avgPrecipitation.isNaN()) true // Return true if average precipitation is NaN
        else avgPrecipitation >= activity.minRain && avgPrecipitation <= activity.maxRain
    }

    /**
     * Checks if the air temperature is suitable for an activity.
     * @param activity The activity to check.
     * @param airTemperature The air temperature.
     * @return True if the air temperature is suitable, false otherwise.
     */
    private fun airTemperatureCheck(activity: UserActivityEntity, airTemperature: Double?): Boolean {
        return airTemperature?.let {
            it >= activity.minTemp && it <= activity.maxTemp
        } ?: true // Return true if air temperature is null
    }

    /**
     * Checks if the wind speed is suitable for an activity.
     *
     * @param activity The activity to check.
     * @param windSpeed The wind speed.
     * @param gustSpeed The wind speed of gust.
     * @return True if the wind speed is suitable, false otherwise.
     */
    private fun windSpeedCheck(activity: UserActivityEntity, windSpeed: Double?, gustSpeed: Double?): Boolean {
        val effectiveWindSpeed = listOfNotNull(windSpeed, gustSpeed).maxOrNull()
        return effectiveWindSpeed?.let {
            it >= activity.minWind && it <= activity.maxWind
        } ?: true // Return true if wind speed is null
    }

    /**
     * Calculates the average precipitation for the next 12 hours.
     * @param weather The weather data.
     * @return The average precipitation.
     */
    private fun getAveragePrecipitation(weather: ForecastTimeStep): Double {
        val precipitationValues = listOfNotNull(
            weather.data.next1Hours?.details?.precipitationAmount,
            weather.data.next6Hours?.details?.precipitationAmount,
            weather.data.next12Hours?.details?.precipitationAmount
        )
        return if (precipitationValues.isEmpty()) Double.NaN // Return NaN if no precipitation data
        else precipitationValues.average()
    }
}
