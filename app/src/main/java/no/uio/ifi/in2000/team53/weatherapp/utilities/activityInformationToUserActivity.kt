package no.uio.ifi.in2000.team53.weatherapp.utilities

import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation

/**
 * Converts an [ActivityInformation] to a [UserActivityEntity].
 * @param activityInformation The [ActivityInformation] to convert.
 * @param selected Whether the activity is selected or not.
 *
 * @return The converted [UserActivityEntity].
 */
fun ActivityInformationtoUserActivityEntity(activityInformation: ActivityInformation, selected: Boolean) = UserActivityEntity(
    activityID = activityInformation.ActivityID,
    activityName = activityInformation.ActivityName,
    maxRain = activityInformation.MaxRain,
    maxTemp = activityInformation.MaxTemp,
    maxWind = activityInformation.MaxWind,
    minRain = activityInformation.MinRain,
    minTemp = activityInformation.MinTemp,
    minWind = activityInformation.MinWind,
    selected = selected
)