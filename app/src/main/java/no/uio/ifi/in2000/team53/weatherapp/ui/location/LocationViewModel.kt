package no.uio.ifi.in2000.team53.weatherapp.ui.location

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.utilities.geoPointToString
import no.uio.ifi.in2000.team53.weatherapp.utilities.stringToGeoPoint
import org.osmdroid.util.GeoPoint
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the Location screen.
 *
 * @param userPreferencesManager [UserPreferencesManager] for handling user preferences.
 * @param application The application context.
 */
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    application: Application
) : AndroidViewModel(application) {

    private val _selectedLocation = MutableStateFlow<GeoPoint?>(null)
    val selectedLocation: StateFlow<GeoPoint?> = _selectedLocation

    private val _searchResults = MutableStateFlow<List<Address>>(emptyList())
    val searchResults: StateFlow<List<Address>> = _searchResults

    private val _currentAddress = MutableStateFlow<String>("")
    val currentAddress: StateFlow<String> = _currentAddress

    init {
        // Initialize with the current location
        viewModelScope.launch {
            val storedLocation = userPreferencesManager.userPreferencesFlow.map { it.location }.firstOrNull()
            storedLocation?.let {
                val geoPoint = stringToGeoPoint(it)
                updateLocation(geoPoint)
                geocodeLocation(geoPoint)
            }
        }
    }

    /**
     * Updates the selected location.
     *
     * @param location The new location.
     */
    fun updateLocation(location: GeoPoint) {
        viewModelScope.launch {
            _selectedLocation.value = location
            geocodeLocation(location)
        }
    }

    /**
     * Stores the selected location in the user preferences.
     */
    fun storeLocation() {
        if (selectedLocation.value != null) {
            val stringLocation = geoPointToString(selectedLocation.value!!)
            viewModelScope.launch {
                userPreferencesManager.updateLocation(stringLocation)
            }
        }
    }

    /**
     * Searches for a location based on the given address.
     *
     * @param address The address to search for.
     */
    @Suppress("DEPRECATION")
    fun searchLocation(address: String) {
        viewModelScope.launch {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val handler = Handler(Looper.getMainLooper())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(address, 5, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        handler.post {
                            _searchResults.value = addresses
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        handler.post {
                            _searchResults.value = emptyList()
                        }
                    }
                })
            } else {
                try {
                    val addresses = geocoder.getFromLocationName(address, 5)
                    _searchResults.value = addresses ?: emptyList()
                } catch (e: Exception) {
                    _searchResults.value = emptyList()
                }
            }
        }
    }

    /**
     * Geocodes the given GeoPoint to get the address.
     *
     * @param location The GeoPoint to geocode.
     */
    @Suppress("DEPRECATION")
    private fun geocodeLocation(location: GeoPoint) {
        viewModelScope.launch {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val handler = Handler(Looper.getMainLooper())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        handler.post {
                            _currentAddress.value = addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        handler.post {
                            _currentAddress.value = "Error finding address"
                        }
                    }
                })
            } else {
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null) {
                        _currentAddress.value = addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
                    }
                } catch (e: Exception) {
                    _currentAddress.value = "Error finding address"
                }
            }
        }
    }
}
