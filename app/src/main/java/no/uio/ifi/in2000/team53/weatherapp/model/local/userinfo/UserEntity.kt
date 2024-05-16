package no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Entity class for storing user information in the local database.
 *
 * @param id the unique identifier for the user.
 * @param name the name of the user.
 * @param profilePictureUri the URI of the user's profile picture.
 */
@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int,
    var name: String,
    var profilePictureUri: String?
)

/**
 * Data access object for the user entity.
 */
@Dao
interface UserDao {

    /**
     * Returns a flow of the user information from the local database.
     *
     * @return Flow of [UserEntity] object.
     */
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUser(): Flow<UserEntity>

    /**
     * Returns a flow of the user name value from the local database
     *
     * @return Flow of [String] object.
     */
    @Query("SELECT name FROM user_profile WHERE id = 1")
    fun getUserName(): Flow<String>

    /**
     * Returns a flow of the user profile picture Uri from the local database
     *
     * @return Flow of [String?] object.
     */
    @Query("SELECT profilePictureUri FROM user_profile WHERE id = 1")
    fun getProfilePicture(): Flow<String?>

    /**
     * Returns the user information from the local database.
     *
     * @return [UserEntity] object.
     */
    @Query("SELECT * FROM user_profile WHERE id = 1") //We are only interested in the first row of the table as we only have 1 user profile.
    suspend fun getUserSuspend(): UserEntity?

    /**
     * Inserts or updates the user information in the local database.
     *
     * @param user the user information to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    /**
     * Updates the user's name in the local database.
     *
     * @param user is a copy of the old entity with the new name of the user.
     */
    @Update
    suspend fun updateName(user: UserEntity)

    /**
     * Updates the user's profile picture URI in the local database.
     *
     * @param profilePictureUri the URI of the user's profile picture.
     */
    @Query("UPDATE user_profile SET profilePictureUri = :profilePictureUri WHERE id = 1")
    suspend fun insertPicture(profilePictureUri: String?)

    /**
     * Initializes the user profile with default data.
     *
     * @param defaultName the new name of the user.
     * @param defaultPic the URI of the user's profile picture.
     */
    @Query("UPDATE user_profile SET id = 1, name =:defaultName, profilePictureUri = :defaultPic")
    suspend fun insertInitialValues(defaultName: String, defaultPic: String?)
}