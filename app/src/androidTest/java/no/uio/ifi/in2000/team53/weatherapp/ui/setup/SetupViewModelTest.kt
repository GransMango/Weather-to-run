package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import android.content.Context
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team53.weatherapp.BaseTest
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserTimesRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.userinfo.UserRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase.ActivityDatabaseRepository
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.random.Random


class SetupViewModelTest : BaseTest() {
    private lateinit var viewModel: SetupViewModel
    @Before
    override fun beforeEach() {
        super.beforeEach()
        val appContext: Context = mockk(relaxed = true)
        val userPreferencesManager: UserPreferencesManager = mockk(relaxed = true)
        val activityDatabaseRepository: ActivityDatabaseRepository = mockk(relaxed = true)
        val userActivityRepository: UserActivityRepository = mockk(relaxed = true)
        val userTimesRepository: UserTimesRepository = mockk(relaxed = true)
        val userRepository: UserRepository = mockk(relaxed = true)
        viewModel = SetupViewModel(appContext, userPreferencesManager, activityDatabaseRepository, userActivityRepository, userTimesRepository,userRepository)
    }
    @After
    override fun afterEach() {
        super.afterEach()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addSelection_increasesSelectedActivitiesSize() = runTest {
        val activity = ActivityInformation(0, "Running", Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        val initialSize = viewModel.selectedActivities.value.size
        viewModel.addSelection(activity)
        val newSize = viewModel.selectedActivities.value.size
        assertEquals(initialSize + 1, newSize)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeSelection_decreasesSelectedActivitiesSize() = runTest {
        val activity = ActivityInformation(0, "Running", Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        viewModel.addSelection(activity)
        val initialSize = viewModel.selectedActivities.value.size
        viewModel.removeSelection(activity)
        val newSize = viewModel.selectedActivities.value.size
        assertEquals(initialSize - 1, newSize)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updatePollenNotification_updatesUserPreferences() = runTest {
        viewModel.updatePollenNotification(true)
        assertTrue(viewModel.preferences.first().pollenNotification)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateLanguage_updatesUserPreferences() = runTest {
        viewModel.updateLanguage("no")
        assertEquals("no", viewModel.preferences.first().language)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateIsFirstLaunch_updatesUserPreferences() = runTest {
        viewModel.updateIsFirstLaunch(true)
        assertTrue(viewModel.preferences.first().firstLaunch)

        viewModel.updateIsFirstLaunch(false)
        assertFalse(viewModel.preferences.first().firstLaunch)
    }




}
