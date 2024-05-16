package no.uio.ifi.in2000.team53.weatherapp.ui.settings

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team53.weatherapp.BaseTest
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.LocalActivitiesDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest : BaseTest() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var localActivitiesDatabase: LocalActivitiesDatabase

    @Before
    override fun beforeEach() {
        super.beforeEach()
        userPreferencesManager = mockk(relaxed = true)
        viewModel = SettingsViewModel(userPreferencesManager,localActivitiesDatabase)
    }

    @After
    override fun afterEach() {
        super.afterEach()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun updateLanguage_changesLanguageInUIState() = runTest {
        viewModel.updateLanguage("Spanish")
        assertEquals("Spanish", viewModel.settingsState.first().language)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateLanguage_emptyString_doesNotChangeLanguageInUIState() = runTest {
        viewModel.updateLanguage("")
        assertEquals("English", viewModel.settingsState.first().language)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun resetApp_setsIsAppResetInUIState() = runTest {
        viewModel.resetAppAndShutdown()
        assertEquals(true, viewModel.settingsState.first().isAppReset)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun resetApp_afterLanguageUpdate_setsIsAppResetInUIStateAndKeepsLanguage() = runTest {
        viewModel.updateLanguage("Spanish")
        viewModel.resetAppAndShutdown()
        val state = viewModel.settingsState.first()
        assertEquals(true, state.isAppReset)
        assertEquals("Spanish", state.language)
    }
}
