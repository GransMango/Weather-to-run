package no.uio.ifi.in2000.team53.weatherapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.LocalActivitiesDatabase
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * UI state for the Settings screen.
 *
 * @param language The selected language.
 * @param isAppReset Whether the app should be reset.
 */
data class SettingsUIState(
    val language: String = "English",
    val isAppReset: Boolean = false
)

/**
 * ViewModel for the Settings screen.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val localActivitiesDatabase: LocalActivitiesDatabase
) : ViewModel() {
    private val _settingsUIState = MutableStateFlow(SettingsUIState())
    val settingsState = _settingsUIState.asStateFlow()

    init {
        viewModelScope.launch {
            val selectedLanguage = userPreferencesManager.getLanguage().first()
            if (selectedLanguage == "en") _settingsUIState.value = _settingsUIState.value.copy(language = "English")
            if (selectedLanguage == "nb") _settingsUIState.value = _settingsUIState.value.copy(language = "Norsk")
        }
    }
    /**
     * Fetch all available languages
     *
     */
    fun getAvailableLanguages(): List<String> {
        return listOf("English", "Norsk")
    }

    /**
     * Fetch the  current language.
     */
    fun getNewLanguage() : String{
        return _settingsUIState.value.language
    }

    /**
     * Update the language.
     *
     * @param language string representing the new language.
     */
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            if(language == "English") userPreferencesManager.updateLanguage("en")
            if(language == "Norsk") userPreferencesManager.updateLanguage("nb")
            if (language.isNotEmpty()) {
                _settingsUIState.value = _settingsUIState.value.copy(language = language)
                userPreferencesManager.updateLanguage(language)
            }
        }
        _settingsUIState.value = _settingsUIState.value.copy(language = language)
    }

    /**
     * Perform app reset by clearing local data and exiting app
     *
     */
    suspend fun resetAppAndShutdown(){
        viewModelScope.launch {
            userPreferencesManager.updateIsFirstLaunch(true)
            _settingsUIState.value = _settingsUIState.value.copy(isAppReset = true)
            withContext(Dispatchers.IO){ //Coroutine on screen -> viewmodelScope -> dispatcher... All that to be able to work on db from "main", but it works now
                localActivitiesDatabase.clearAllTables()
            }
            delay(1500)
            exitProcess(0) //Should terminate the app process
        }
    }
}