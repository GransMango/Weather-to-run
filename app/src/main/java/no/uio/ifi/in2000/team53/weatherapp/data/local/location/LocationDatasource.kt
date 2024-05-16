package no.uio.ifi.in2000.team53.weatherapp.data.local.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Provides functionality to obtain the device's current location.
 *
 * @property application The instance of [Application] used to check permissions.
 * @property fusedLocationClient The instance of [FusedLocationProviderClient] used to fetch the location data.
 */
class LocationDatasource @Inject constructor(
    private val application: Application,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    /**
     * Retrieves the current location of the device with balanced power accuracy.
     *
     * @return The current location as a [Location] object or null if the location permission is not granted or an exception occurs.
     */
    suspend fun getCurrentLocation(): Location? {
        val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        return if (hasLocationPermission()) {
            try {
                fusedLocationClient.getCurrentLocation(accuracy, CancellationTokenSource().token).await()
            } catch (e: SecurityException) {
                Log.d("LocationDataSourceImpl", "SecurityException: ${e.message}")
                null
            }
        } else {
            Log.d("LocationDataSourceImpl", "No location permission")
            null
        }
    }

    /**
     * Checks if the application has the necessary location permissions.
     *
     * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted, false otherwise.
     */
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
