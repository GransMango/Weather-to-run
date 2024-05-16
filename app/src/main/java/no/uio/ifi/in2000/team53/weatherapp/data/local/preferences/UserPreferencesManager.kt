package no.uio.ifi.in2000.team53.weatherapp.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.uio.ifi.in2000.team53.weatherapp.model.local.preferences.UserPreferences

/**
 * Extension to provide easier access to the preferences DataStore from the Context.
 */
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

/**
 * Manages user preferences stored in DataStore.
 *
 * Provides methods to update user preferences such as theme, language, and notifications,
 * and exposes a flow to observe changes in user preferences.
 *
 * @param context the context from which the DataStore is accessed.
 */
class UserPreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    /**
     * A flow of [UserPreferences] reflecting the current preferences stored in the DataStore.
     * It maps stored preference values into a [UserPreferences] object, providing default
     * values where preferences have not been previously set.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            language = preferences[PreferencesKeys.LANGUAGE] ?: "en", // Default to English
            pollenNotification = preferences[PreferencesKeys.POLLENNOTIFICATION] ?: false, // Default to false
            firstLaunch = preferences[PreferencesKeys.FIRST_LAUNCH] ?: true, // Default to true
            location = preferences[PreferencesKeys.LOCATION] ?: "59.9, 10.75" // Default to Oslo
        )
    }

    /**
     * Updates the 'first launch' preference in the DataStore. The 'first launch' value determines whether
     * the user gets the welcome screen or the home screen on app launch.
     *
     * @param isFirstLaunch the new value for the first launch preference.
     */
    suspend fun updateIsFirstLaunch(isFirstLaunch: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_LAUNCH] = isFirstLaunch
        }
    }

    /**
     * Updates the 'language' preference in the DataStore.
     *
     * @param language the new language setting, typically an ISO language code.
     */
    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    /**
     * Fetches the 'language' preference from the DataStore.
     */
    fun getLanguage() :Flow<String>{
        return dataStore.data.map {
            it[PreferencesKeys.LANGUAGE] ?: "en"
        }
    }

    /**
     * Updates the 'pollen notification' preference in the DataStore.
     *
     * @param pollenNotification the new value for the pollen notification preference.
     */
    suspend fun updatePollenNotification(pollenNotification: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.POLLENNOTIFICATION] = pollenNotification
        }
    }

    /**
     * Updates the 'location' preference in the DataStore.
     *
     * @param location the new location setting, typically a string in the format "latitude, longitude".
     */
    suspend fun updateLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOCATION] = location
        }
    }

    /**
     * Defines keys used for accessing and storing user preferences in the DataStore.
     */
    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
        val POLLENNOTIFICATION = booleanPreferencesKey("pollen_notification")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val LOCATION = stringPreferencesKey("location")
    }
}
