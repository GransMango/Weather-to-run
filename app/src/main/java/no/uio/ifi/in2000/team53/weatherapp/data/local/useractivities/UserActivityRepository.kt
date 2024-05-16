package no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import javax.inject.Inject

/**
 * Repository for managing user activities in the local database.
 *
 * @property userActivityDao The DAO for user activities.
 */
class UserActivityRepository @Inject constructor(private val userActivityDao: UserActivityDao) {


    /**
     * Retrieves a user activity by its name from the database.
     * @param name The name of the user activity to retrieve.
     * @return The user activity entity with the specified name.
     */
    suspend fun getActivityByName(name: String): UserActivityEntity {
        return userActivityDao.getActivityByName(name)
    }

    /**
     * Retrieves all user activities from the local database.
     *
     * @return A flow of all user activities.
     */
    fun getAllUserActivities(): Flow<List<UserActivityEntity>> {
        return userActivityDao.getAllUserActivities()
    }

    /**
     * Inserts a user activity into the local database.
     *
     * @param userActivity The user activity to insert.
     */
    suspend fun insertUserActivity(userActivity: UserActivityEntity) {
        userActivityDao.insertUserActivity(userActivity)
    }

    /**
     * Updates a user activity in the local database.
     *
     * @param userActivity The user activity to update.
     */
    suspend fun updateUserActivity(userActivity: UserActivityEntity) {
        userActivityDao.updateUserActivity(userActivity)
    }

    /**
     * Deletes a user activity from the local database.
     *
     * @param userActivity The user activity to delete.
     */
    suspend fun deleteUserActivity(userActivity: UserActivityEntity) {
        userActivityDao.deleteUserActivity(userActivity.activityID)
    }

}