package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import javax.inject.Inject

/**
 * ViewModel for the User Activities screen.
 *
 * @param userActivityRepository [UserActivityRepository] for handling user activity data.
 */
@HiltViewModel
class UserActivitiesViewModel @Inject constructor(
    private val userActivityRepository: UserActivityRepository,
) : ViewModel() {

    private val _userActivitiesState = MutableStateFlow(emptyList<UserActivityEntity>())
    val userActivitiesState: StateFlow<List<UserActivityEntity>> = _userActivitiesState.asStateFlow()

    init {
        loadActivities()
    }

    /**
     * Load the user activities.
     */
    private fun loadActivities() {
        viewModelScope.launch {
            val localActivities = userActivityRepository.getAllUserActivities().first()
            _userActivitiesState.value = localActivities
        }
    }
}
