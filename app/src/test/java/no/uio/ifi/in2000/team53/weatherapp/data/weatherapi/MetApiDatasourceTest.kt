package no.uio.ifi.in2000.team53.weatherapp.data.weatherapi

import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetApiDatasource
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// Run with makes it possible to use log.d in functions while testing
@RunWith(RobolectricTestRunner::class)
class MetApiDatasourceTest {
    private val metApiDatasource = MetApiDatasource()
    @Test
    fun responseNotEmpty() = runTest{
        val response = metApiDatasource.getWeather()
        assert(response.properties.timeseries.isNotEmpty())
    }

}

