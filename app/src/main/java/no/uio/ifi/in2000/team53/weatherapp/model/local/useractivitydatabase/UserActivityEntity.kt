package no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Entity class for storing user activities in the local database.
 *
 * @param activityID the unique identifier for the activity.
 * @param activityName the name of the activity.
 * @param maxRain the maximum amount of rain that is acceptable for the activity.
 * @param maxTemp the maximum temperature that is acceptable for the activity.
 * @param maxWind the maximum wind speed that is acceptable for the activity.
 * @param minRain the minimum amount of rain that is acceptable for the activity.
 * @param minTemp the minimum temperature that is acceptable for the activity.
 * @param minWind the minimum wind speed that is acceptable for the activity.
 * @param selected boolean indicating if the activity is selected by the user.
 */
@Entity(tableName = "user_activities")
data class UserActivityEntity(
    @PrimaryKey val activityID: Int,
    val activityName: String,
    val maxRain: Double,
    val maxTemp: Double,
    val maxWind: Double,
    val minRain: Double,
    val minTemp: Double,
    val minWind: Double,
    val selected: Boolean = false
)

/**
 * Data access object for the user activities entity.
 */
@Dao
interface UserActivityDao {
    /**
     * Returns a flow of all user activities from the local database.
     *
     * @return Flow of list of [UserActivityEntity] objects.
     */
    @Query("SELECT * FROM user_activities")
    fun getAllUserActivities(): Flow<List<UserActivityEntity>>

    /**
     * Inserts a new user activity into the local database.
     *
     * @param userActivity the user activity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserActivity(userActivity : UserActivityEntity)

    /**
     * Updates an existing user activity in the local database.
     *
     * @param userActivity the user activity to update.
     */
    @Update
    suspend fun updateUserActivity(userActivity: UserActivityEntity)

    /**
     * Fetches a user activity by its name.
     *
     * @param name the name of the activity to fetch.
     * @return the [UserActivityEntity] object with the specified name.
     */
    @Query("SELECT * FROM user_activities WHERE activityName = :name LIMIT 1")
    suspend fun getActivityByName(name: String): UserActivityEntity

    /**
     * Deletes a user activity from the local database.
     *
     * @param activityID the user activity to delete.
     */
    @Query("DELETE FROM user_activities WHERE activityID = :activityID")
    suspend fun deleteUserActivity(activityID: Int)

}