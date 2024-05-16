package no.uio.ifi.in2000.team53.weatherapp.data.local.userinfo

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo.UserDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo.UserEntity
import javax.inject.Inject

/**
 * Repository for managing user information in the local database.
 *
 * @property userDao The DAO for user information.
 */
class UserRepository @Inject constructor(private val userDao: UserDao) {

    /**
     * Retrieves the user's name from the local database.
     *
     * @return A flow of the user's name.
     */
    fun getUserName(): Flow<String> {
        return userDao.getUserName()
    }

    /**
     * Retrieves the user's profile picture URI from the local database.
     *
     * @return A flow of the user's profile picture URI.
     */
    fun getProfilePictureUri(): Flow<String?>{
        return userDao.getProfilePicture()
    }

    /**
     * Retrieves the user data from the local database.
     *
     * @return A flow of the user data
     */
    fun getUser(): Flow<UserEntity>{
        return userDao.getUser()
    }

    /**
     * Updates the user's name in the local database.
     *
     * @param user The entity containing the new name to set for the user.
     */
    suspend fun updateName(user: UserEntity) {
        userDao.updateName(user)
    }

    /**
     * Updates the user's profile picture URI in the local database.
     *
     * @param profilePictureUri The new profile picture URI to set for the user.
     */
    suspend fun updateProfilePicture(profilePictureUri: String) {
        userDao.insertPicture(profilePictureUri)
    }

    /**
     * Inserts initial values for the user profile
     */
    suspend fun setInitialValues(){
        userDao.insertOrUpdateUser(UserEntity(1,"",null))
    }
}