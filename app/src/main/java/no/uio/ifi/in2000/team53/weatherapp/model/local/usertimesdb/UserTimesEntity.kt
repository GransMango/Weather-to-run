package no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Entity class for storing user times in the local database.
 *
 * @param id the unique identifier for the user times.
 * @param day the day of the week.
 * @param listOfTimes the list of times for the day.
 */
@Entity(tableName = "user_times")
data class UserTimesEntity(
    @PrimaryKey val id: Int,
    var day: String,
    var listOfTimes: Pair<String, String>? //OBS: Ensure correct user input in viewmodel.
)

/**
 * Data access object for the user times entity.
 */
@Dao
interface UserTimesDao {
    /**
     * Returns a flow of all user times from the local database.
     *
     * @return Flow of list of [UserTimesEntity] objects.
     */
    @Query("SELECT * FROM user_times") //Can also change this to return the list of times
    fun getAllUserTimes(): Flow<List<UserTimesEntity?>>

    /*
    @Query("SELECT listOfTimes FROM user_times WHERE day = :givenDay")
    suspend fun getTimesForDay(givenDay: String): UserTimesEntity?


     */

    /**
     * Inserts a new list of times for a given day into the local database.
     *
     * @param givenDay the day of the week.
     * @param newTimes the new list of times for the day.
     */
    @Query("UPDATE user_times SET listOfTimes = :newTimes WHERE day = :givenDay")
    suspend fun insertTimes(givenDay: String, newTimes : Pair<String,String>)

    /**
     * Removes the list of times for a given day from the local database.
     *
     * @param givenDay the day of the week.
     */
    @Query("UPDATE user_times SET listOfTimes = null WHERE day = :givenDay")
    suspend fun removeTimesForDay(givenDay: String)

    /**
     * Inserts the initial list of times into the local database.
     *
     * @param initialData the initial list of times.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInitialTimes(initialData: List<UserTimesEntity>)

    /**
     * Checks if the database is empty.
     *
     * @return the number of days in the database.
     */
    @Query("SELECT COUNT(day) FROM user_times")
    suspend fun checkDays(): Int

}