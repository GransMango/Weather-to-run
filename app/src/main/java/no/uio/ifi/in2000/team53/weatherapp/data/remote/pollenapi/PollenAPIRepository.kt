package no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi

import androidx.compose.runtime.mutableStateOf
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.PollenData
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.RegionPollenData
import javax.inject.Inject


/**
 * Repository for managing the retrieval of pollen-related data via [PollenAPIDatasource].
 * Provides functions to load data from the datasource and store it in observable state holders,
 * enabling UI components to react to data changes.
 *
 * @property datasource the data source for fetching pollen data from a remote API.
 */
class PollenAPIRepository @Inject constructor(private val datasource: PollenAPIDatasource)  {
    private val regions = mutableStateOf<List<PollenData>?>(null)
    private val pollenData = mutableStateOf<RegionPollenData?>(null)

    /**
     * Loads pollen region data from the remote source and updates the [regions] state holder.
     * This function fetches a list of pollen regions, pollen data and updates the state to reflect any new data.
     */
    suspend fun loadPollenRegions() {
        val response = datasource.getPollenRegions()
        regions.value = response
    }

    /**
     * Loads specific pollen data for a given region defined by latitude and longitude.
     * Updates the [pollenData] state holder with the result.
     *
     * @param lat the latitude of the region for which pollen data is requested.
     * @param lon the longitude of the region for which pollen data is requested.
     */
    suspend fun loadPollenDataForRegion(lat: Double, lon: Double) {
        val response = datasource.getPollenDataForRegion(lat, lon)
        pollenData.value = response
    }

    /**
     * Retrieves the pollen data for the region specified in the [pollenData] state holder.
     * @return the pollen data for the region, or null if no data is available.
     */
    fun getPollenData(): PollenData? {
        val desiredRegionName = pollenData.value?.kommune
        val regionPollenData = pollenData.value
        if (pollenData.value == null) {
            return null
        }
        return regionPollenData?.let { data ->
            data.pollen_data.find {
                it.displayName.contains(
                    desiredRegionName ?: "",
                    ignoreCase = true
                )
            }
        }
    }
}
