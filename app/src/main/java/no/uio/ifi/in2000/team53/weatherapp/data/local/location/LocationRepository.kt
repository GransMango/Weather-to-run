package no.uio.ifi.in2000.team53.weatherapp.data.local.location

import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import javax.inject.Inject

/**
 * Manages access to the device's current location through the LocationDataSource.
 *
 * @property locationDataSource The data source from which location data is obtained.
 */
class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDatasource,
    private val userPreferencesManager: UserPreferencesManager
){
    /**
     * Gets the current location of the device. If the location permission is granted, the current
     * location is obtained from the LocationDataSource. If the location permission is not granted,
     * the stored location is returned.
     *
     * @return a string representation of the current location, or the stored location if the current
     * location is not available.
     */
    suspend fun getCurrentLocation(): String {
        return if (hasLocationPermission()) {
            val currentLocation = locationDataSource.getCurrentLocation()
            currentLocation?.let {
                "${it.latitude},${it.longitude}"
            } ?: getStoredLocation()
        } else {
            getStoredLocation()
        }
    }

    /**
     * Gets the stored location from the UserPreferencesManager.
     *
     * @return a string representation of the stored location.
     */
    private suspend fun getStoredLocation(): String {
        return userPreferencesManager.userPreferencesFlow.first().location
    }
    /**
     * Delegates to check if the location permissions are granted.
     *
     * @return true if location permissions are granted, false otherwise.
     */
    fun hasLocationPermission(): Boolean {
        return locationDataSource.hasLocationPermission()
    }
}
