package no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel

import kotlinx.serialization.Serializable

/**
 * Data class for storing remote pollen data.
 *
 * @param displayName String representing the display name of the pollen region.
 * @param id String representing the ID of the pollen region.
 * @param textForecast String representing the text forecast of the pollen data.
 */
@Serializable
data class PollenData(
    val displayName: String,
    val id: String,
    val textForecast: String
)

/**
 * Data class for storing remote region pollen data.
 *
 * @param kommune String representing the kommune of the lat lon the user passed in.
 * @param pollen_data List of [PollenData] representing the pollen data.
 */
@Serializable
data class RegionPollenData(
    val kommune: String,
    val pollen_data: List<PollenData>
)