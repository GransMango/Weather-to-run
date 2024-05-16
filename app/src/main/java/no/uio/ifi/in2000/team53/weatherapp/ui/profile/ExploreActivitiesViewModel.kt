package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase.ActivityDatabaseRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import no.uio.ifi.in2000.team53.weatherapp.utilities.ActivityInformationtoUserActivityEntity
import javax.inject.Inject

/**
 * ViewModel for the Explore Activities screen.
 *
 * @param userActivityRepository [UserActivityRepository] for handling user activity data.
 * @param activityDatabaseRepository [ActivityDatabaseRepository] for handling activity database data.
 */
@HiltViewModel
class ExploreActivitiesViewModel @Inject constructor(
    private val userActivityRepository: UserActivityRepository,
    private val activityDatabaseRepository: ActivityDatabaseRepository
) : ViewModel() {

    private val _userActivitiesState = MutableStateFlow<List<UserActivityEntity>>(emptyList())
    val userActivitiesState: StateFlow<List<UserActivityEntity>> = _userActivitiesState.asStateFlow()

    private val _remoteActivities = MutableStateFlow<List<ActivityInformation>>(emptyList())
    val remoteActivities: StateFlow<List<ActivityInformation>> = _remoteActivities.asStateFlow()

    private val _selectedActivities = MutableStateFlow<List<ActivityInformation>>(emptyList())
    val selectedActivities: StateFlow<List<ActivityInformation>> = _selectedActivities.asStateFlow()

    init {
        loadActivities()
    }

    /**
     * Load the user-selected activities and remote activities.
     */
    private fun loadActivities() {
        viewModelScope.launch {
            // Load local user-selected activities
            userActivityRepository.getAllUserActivities().collect { localActivities ->
                _userActivitiesState.value = localActivities
                // Load remote activities
                loadRemoteActivities(localActivities)
            }
        }
    }

    /**
     * Load remote activities that are not already selected by the user.
     *
     * @param localActivities List of [UserActivityEntity] representing the user-selected activities.
     */
    private fun loadRemoteActivities(localActivities: List<UserActivityEntity>) {
        viewModelScope.launch {
            try {
                activityDatabaseRepository.loadActivities()
                activityDatabaseRepository.observeActivities()
                    .collect { remoteActivities ->
                        val filteredActivities = remoteActivities.filter { remoteActivity ->
                            localActivities.none { localActivity ->
                                localActivity.activityID == remoteActivity.ActivityID
                            }
                        }
                        _remoteActivities.value = filteredActivities
                    }
            } catch (e: Exception) {
            _remoteActivities.value = listOf()
            }
        }
    }

    /**
     * Add an activity to the user's selection.
     *
     * @param activity [ActivityInformation] representing the activity to add.
     */
    fun addSelection(activity: ActivityInformation) {
        _selectedActivities.value = _selectedActivities.value + activity
    }

    /**
     * Remove an activity from the user's selection.
     *
     * @param activity [ActivityInformation] representing the activity to remove.
     */
    fun removeSelection(activity: ActivityInformation) {
        _selectedActivities.value = _selectedActivities.value - activity
    }

    /**
     * Update the user's selection of activities.
     *
     * @param selectedActivityIDs List of Int representing the IDs of the selected activities.
     */
    fun updateUserSelection(selectedActivityIDs: List<Int>) {
        viewModelScope.launch {
            val selectedActivities = _remoteActivities.value.filter { it.ActivityID in selectedActivityIDs }
            selectedActivities.forEach { activityInfo ->
                val userActivityEntity = ActivityInformationtoUserActivityEntity(activityInfo, selected = true)
                userActivityRepository.insertUserActivity(userActivityEntity)
            }
        }
    }
}
