package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserTimesRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.userinfo.UserRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase.ActivityDatabaseRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.preferences.UserPreferences
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import no.uio.ifi.in2000.team53.weatherapp.utilities.ActivityInformationtoUserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.utilities.NotificationHelper
import javax.inject.Inject

/**
 * ViewModel for the Setup screen.
 *
 * @param appContext The application context.
 * @param userPreferencesManager [UserPreferencesManager] for handling user preferences.
 * @param activityDatabaseRepository [ActivityDatabaseRepository] for handling activity data.
 * @param userActivityRepository [UserActivityRepository] for handling user activity data.
 * @param userTimesRepository [UserTimesRepository] for handling user times data.
 */
@HiltViewModel
class SetupViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val userPreferencesManager: UserPreferencesManager,
    private val activityDatabaseRepository: ActivityDatabaseRepository,
    private val userActivityRepository: UserActivityRepository,
    private val userTimesRepository: UserTimesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _preferences = MutableStateFlow(UserPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    private val _activities = MutableStateFlow<List<ActivityInformation>>(emptyList())
    val activities: StateFlow<List<ActivityInformation>> = _activities.asStateFlow()

    private val _selectedActivities = MutableStateFlow<List<ActivityInformation>>(emptyList())
    val selectedActivities: StateFlow<List<ActivityInformation>> = _selectedActivities.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                //This warning is wrong, value will be null on first start
                if(userRepository.getUserName().first().isNullOrEmpty()) userRepository.setInitialValues()

                // Load remote activities
                activityDatabaseRepository.loadActivities()
                activityDatabaseRepository.observeActivities()
                    .collect { remoteActivities ->
                        _activities.value = remoteActivities.take(10)
                    }
                // Collect user preferences
                userPreferencesManager.userPreferencesFlow.collect {
                    _preferences.value = it
                }
            } catch (e: Exception) {
                Log.e("SetupViewModel", "Error loading activities: $e")
            }
        }
    }

    /**
     * Add an activity to the selected activities list.
     *
     * @param activity [ActivityInformation] to add.
     */
    fun addSelection(activity: ActivityInformation) {
        _selectedActivities.value = _selectedActivities.value + activity
    }

    /**
     * Remove an activity from the selected activities list.
     *
     * @param activity [ActivityInformation] to remove.
     */
    fun removeSelection(activity: ActivityInformation) {
        _selectedActivities.value = _selectedActivities.value - activity
    }

    /**
     * Update the user's selected activities.
     *
     * @param selectedActivityIDs List of selected activity IDs.
     */
    fun updateUserSelection(selectedActivityIDs: List<Int>) {
        viewModelScope.launch {
            val selectedActivities = _activities.value.filter { it.ActivityID in selectedActivityIDs }
            selectedActivities.forEach { activityInfo ->
                val userActivityEntity = ActivityInformationtoUserActivityEntity(activityInfo, selected = true)
                userActivityRepository.insertUserActivity(userActivityEntity)
                Log.d("SetupViewModel", "Updated user selection for ${activityInfo.ActivityName}")
            }
        }
    }

    /**
     * Update the user's pollen notification preference.
     *
     * @param pollenNotification Boolean representing the new preference.
     */
    fun updatePollenNotification(pollenNotification: Boolean) {
        viewModelScope.launch {
            _preferences.value = _preferences.value.copy(pollenNotification = pollenNotification)
            userPreferencesManager.updatePollenNotification(pollenNotification)
        }
    }

    /**
     * Update the user's language preference.
     *
     * @param language String representing the new language.
     */
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            _preferences.value = _preferences.value.copy(language = language)
            userPreferencesManager.updateLanguage(language)
        }
    }

    /**
     * Update the user's first launch variable.
     *
     * @param isFirstLaunch Boolean representing the new preference.
     */
    fun updateIsFirstLaunch(isFirstLaunch: Boolean) {
        viewModelScope.launch {
            _preferences.value = _preferences.value.copy(firstLaunch = isFirstLaunch)
            userPreferencesManager.updateIsFirstLaunch(isFirstLaunch)
        }
    }

    /**
     * Schedule a notification after a delay.
     *
     * @param delayMillis Long representing the delay in milliseconds.
     */
    fun scheduleNotification(delayMillis: Long) {
        viewModelScope.launch {
            delay(delayMillis)  // Delay for specified milliseconds
            NotificationHelper.showNotification(
                context = appContext,
                title = "Welcome to WeatherApp",
                message = "Thanks for setting up your preferences!"
            )
        }
    }

    /**
     * Update the user's selected times.
     *
     * @param startTime String representing the start time.
     * @param endTime String representing the end time.
     */
    fun updateUserTimes(startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                for (day in daysOfWeek) {
                    userTimesRepository.addTimeForDay(day, Pair(startTime, endTime))
                    Log.d("SetupViewModel", "Times updated successfully for $day")
                }
            } catch (e: Exception) {
                Log.e("SetupViewModel", "Failed to update times: $e")
            }
        }
    }
}
