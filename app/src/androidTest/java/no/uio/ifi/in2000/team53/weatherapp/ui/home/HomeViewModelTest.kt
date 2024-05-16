package no.uio.ifi.in2000.team53.weatherapp.ui.home

import android.icu.util.Calendar
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team53.weatherapp.BaseTest
import no.uio.ifi.in2000.team53.weatherapp.data.local.location.LocationRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetAPIRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeViewModelTest : BaseTest() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var mockCalendar: Calendar
    private val locationRepository: LocationRepository = mockk(relaxed = true)
    private val metAPIRepository: MetAPIRepository = mockk(relaxed = true)
    @Before
    override fun beforeEach() {
        super.beforeEach()
        viewModel = HomeViewModel(metAPIRepository, locationRepository)
    }
    @After
    override fun afterEach() {
        super.afterEach()
    }
    @ExperimentalCoroutinesApi
    @Test
    fun morningTest() = runTest {
        mockCalendar = mockk()

        every { mockCalendar.get(Calendar.HOUR_OF_DAY) } returns 9

        viewModel = HomeViewModel(metAPIRepository, locationRepository)

        viewModel.initializeGreetingText(mockCalendar)

        advanceUntilIdle()

        assertEquals("God morgen", viewModel.greetingText.value)
    }
    @ExperimentalCoroutinesApi
    @Test
    fun afternoonTest() = runTest {
        mockCalendar = mockk()

        every { mockCalendar.get(Calendar.HOUR_OF_DAY) } returns 15

        viewModel = HomeViewModel(metAPIRepository, locationRepository)

        viewModel.initializeGreetingText(mockCalendar)

        advanceUntilIdle()

        assertEquals("God ettermiddag", viewModel.greetingText.value)
    }
}
