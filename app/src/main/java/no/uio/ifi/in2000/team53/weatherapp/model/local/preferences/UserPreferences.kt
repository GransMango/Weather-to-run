package no.uio.ifi.in2000.team53.weatherapp.model.local.preferences

/**
 * Data class for storing user preferences.
 *
 * @param language String indicating the user's preferred language.
 * @param pollenNotification Boolean indicating if the user wants to receive pollen notifications.
 * @param firstLaunch Boolean indicating if the app is launched for the first time.
 * @param location The user's preferred location.
 */
data class UserPreferences(
    val language: String = "en",
    val pollenNotification: Boolean = false,
    val firstLaunch: Boolean = true,
    val location: String = "59.9, 10.75" // Lat, lon format
)
