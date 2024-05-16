package no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase
import kotlinx.serialization.*

/**
 * Data class for storing remote activity information.
 *
 * @param ActivityID Int representing the activity's ID.
 * @param ActivityName String representing the activity's name.
 * @param MaxRain Double representing the maximum amount of rain allowed for the activity.
 * @param MaxTemp Double representing the maximum temperature allowed for the activity.
 * @param MaxWind Double representing the maximum wind speed allowed for the activity.
 * @param MinRain Double representing the minimum amount of rain allowed for the activity.
 * @param MinTemp Double representing the minimum temperature allowed for the activity.
 * @param MinWind Double representing the minimum wind speed allowed for the activity.
 */
@Serializable
data class ActivityInformation(
    val ActivityID: Int,
    val ActivityName: String,
    val MaxRain: Double,
    val MaxTemp: Double,
    val MaxWind: Double,
    val MinRain: Double,
    val MinTemp: Double,
    val MinWind: Double
)