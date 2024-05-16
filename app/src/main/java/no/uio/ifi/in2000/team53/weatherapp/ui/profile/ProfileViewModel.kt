package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.userinfo.UserRepository
import javax.inject.Inject

/**
 * ViewModel for the Profile screen.
 *
 * @param userRepository [UserRepository] for handling user data.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    var name: Flow<String> = userRepository.getUserName()
    val profilePictureUri: Flow<String?> = userRepository.getProfilePictureUri()

    /**
     * Set the profile name.
     *
     * @param newName String representing the new name.
     */
    fun setProfileName(newName: String) {
        viewModelScope.launch {
            //Log.d(newName, "Name received by viewmodel $newName")
            val user = userRepository.getUser().first()
            userRepository.updateName(user.copy(name = newName))
        }
    }

    /**
     * Set the profile picture.
     *
     * @param profilePictureUri String representing the new profile picture URI.
     */
    fun setProfilePicture(profilePictureUri: String) {
        viewModelScope.launch {
            userRepository.updateProfilePicture(profilePictureUri)
        }
    }
}