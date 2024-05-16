package no.uio.ifi.in2000.team53.weatherapp.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import javax.inject.Inject

/**
 * ViewModel for the splash screen.
 *
 * @param userPreferencesManager [UserPreferencesManager] for handling user preferences.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(userPreferencesManager: UserPreferencesManager) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo
    private val userPreferencesFlow = userPreferencesManager.userPreferencesFlow
    var startupLanguage = ""

    init {
        viewModelScope.launch {
            delay(2000)  // Wait for 2 seconds
            // Here we should load data, check if first launch, etc.
            startupLanguage = userPreferencesFlow.first().language
            if (userPreferencesFlow.first().firstLaunch) {
                Log.d(userPreferencesFlow.first().firstLaunch.toString(), "Should be true on first start")
                _navigateTo.value = "welcome"
            } else {
                Log.d(userPreferencesFlow.first().firstLaunch.toString(), "else we go to homescreen from splash")
                _navigateTo.value = "home"
            }
        }
    }
}
