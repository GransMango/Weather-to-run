package no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesEntity
import javax.inject.Inject


/**
 * Repository for managing user notification times in the local database.
 *
 * @property userTimesDao The DAO for user times.
 */
class UserTimesRepository @Inject constructor(private val userTimesDao: UserTimesDao) {

    /**
     * Retrieves all user notification times from the local database.
     *
     * @return A flow of all user notification times.
     */
    fun getAllUserTimes(): Flow<List<UserTimesEntity?>> {
        return userTimesDao.getAllUserTimes()
    }

    /**
     * Retrieves the notification times for a specific day from the local database.
     *
     * @param day The day for which to retrieve the notification times.
     * @return A flow of the notification times for the specified day.
     */
    suspend fun addTimeForDay(day: String, newTime: Pair<String,String>) {
        userTimesDao.insertTimes(day, newTime)
    }

    /**
     * Checks if the days are already in the table, if not, it inserts them.
     * This is used to ensure that the table always has the days of the week.
     */
    suspend fun checkDaysInTable(){
        if (userTimesDao.checkDays() != 7){
            val initialUserTimes = listOf(
                UserTimesEntity(0, "Mon", null),
                UserTimesEntity(1, "Tue", null),
                UserTimesEntity(2, "Wed", null),
                UserTimesEntity(3, "Thu", null),
                UserTimesEntity(4, "Fri", null),
                UserTimesEntity(5, "Sat", null),
                UserTimesEntity(6, "Sun", null)
            )

            userTimesDao.insertInitialTimes(initialUserTimes)
        }
    }
}