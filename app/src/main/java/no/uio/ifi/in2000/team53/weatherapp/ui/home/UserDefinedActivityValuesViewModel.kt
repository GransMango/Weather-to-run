package no.uio.ifi.in2000.team53.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import javax.inject.Inject

/**
 * View model class for handling user-defined activities.
 * @param userActivityRepository The repository for user activity data operations.
 */
@HiltViewModel
class UserDefinedActivityValuesViewModel @Inject constructor(
    private val userActivityRepository: UserActivityRepository
) : ViewModel() {

    private val _filteredActivity = MutableStateFlow<UserActivityEntity?>(null)
    val filteredActivity: StateFlow<UserActivityEntity?> = _filteredActivity

    /**
     * Retrieves a filtered user activity by name from the repository.
     * @param name The name of the user activity to retrieve.
     */

    fun getFilteredActivityByName(name: String) {
        viewModelScope.launch {
            val activity = userActivityRepository.getActivityByName(name)
            _filteredActivity.value = activity
        }
    }

    /**
     * Updates a user activity in the repository.
     * @param activity The user activity to update.
     */

    fun updateActivity(activity: UserActivityEntity) {
        viewModelScope.launch {
            userActivityRepository.updateUserActivity(activity)
        }
    }

    fun deleteActivity(activity: UserActivityEntity) {
        viewModelScope.launch {
            userActivityRepository.deleteUserActivity(activity)
        }
    }
}