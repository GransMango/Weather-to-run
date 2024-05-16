package no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi

import javax.inject.Inject
import no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis.ForecastTimeStep
import androidx.compose.runtime.mutableStateOf

/**
 * Repository for managing the retrieval of weather data via [MetApiDatasource].
 * Provides functions to load data from the datasource and store it in observable state holders,
 * enabling UI components to react to data changes.
 *
 * @property datasource the data source for fetching weather data from a remote API.
 */
class MetAPIRepository @Inject constructor(private val datasource: MetApiDatasource) {
    private val forecast = mutableStateOf<List<ForecastTimeStep>?>(null)

    /**
     * Loads weather data from the remote source and updates the [forecast] state holder.
     * This function fetches a list of weather forecast data and updates the state to reflect any new data.
     */
    suspend fun loadWeather() {
        val response = datasource.getWeather()
        forecast.value = response.properties.timeseries
    }

    /**
     * Loads weather data from the remote source for a specific location defined by latitude and longitude.
     * Updates the [forecast] state holder with the result.
     *
     * @param latitude the latitude of the location for which weather data is requested.
     * @param longitude the longitude of the location for which weather data is requested.
     */
    suspend fun loadWeather(latitude: Double, longitude: Double) {
        val response = datasource.getWeather(latitude, longitude)
        forecast.value = response.properties.timeseries
    }

    /**
     * Provides a read-only view of the forecast previously loaded from the remote server.
     *
     * @return A list of [ForecastTimeStep] objects representing the forecast data.
     */
    fun getForecast() : List<ForecastTimeStep> {
        return forecast.value ?: emptyList()
    }
}