package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserTimesRepository
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesEntity
import javax.inject.Inject

/**
 * ViewModel for the User Times screen.
 *
 * @param userTimesRepository [UserTimesRepository] for handling user times data.
 */
@HiltViewModel
class UserTimesViewModel @Inject constructor(private val userTimesRepository: UserTimesRepository) : ViewModel() {
    val userTimes: Flow<List<UserTimesEntity?>> = userTimesRepository.getAllUserTimes()

    /**
     * Load the default time values if none in Database.
     */
    fun checkDaysInTable(){
        viewModelScope.launch {
            userTimesRepository.checkDaysInTable()
        }
    }

    /**
     * Add time for selected day.
     *
     * @param day String representing the day to add time for.
     * @param newTime Pair of Strings representing the new time to add.
     */
    fun addTimeForDay(day: String, newTime: Pair<String,String>) {
        viewModelScope.launch {
            userTimesRepository.addTimeForDay(day, newTime)
        }
    }

    /*
    //Allow user to remove time for selected day. Maybe on click?
    fun removeTimesForDay(day: String) {
        viewModelScope.launch {
            userTimesRepository.removeTimesForDay(day)
        }
    }
     */
}