package no.uio.ifi.in2000.team53.weatherapp.data.pollenapi

import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi.PollenAPIDatasource
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PollenAPIDatasourceTest {
    private val pollenAPIDatasource : PollenAPIDatasource = PollenAPIDatasource()
    @Test
    fun responseNotEmpty() = runTest{
        val response = pollenAPIDatasource.getPollenRegions()
        assert(response.isNotEmpty())
    }

    @Test
    fun responseNotEmpty2() = runTest{
        val response = pollenAPIDatasource.getPollenDataForRegion(59.9, 10.75)
        assert(response.kommune == "Oslo")
    }
}
