package no.uio.ifi.in2000.team53.weatherapp.utilities

import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityInformationToUserActivityEntityTest {

    @Test
    fun convertsActivityInformationToUserActivityEntityWithSelectedTrue() {
        val activityInformation = ActivityInformation(
            ActivityID = 1,
            ActivityName = "Running",
            MaxRain = 10.0,
            MaxTemp = 30.0,
            MaxWind = 5.0,
            MinRain = 0.0,
            MinTemp = 15.0,
            MinWind = 0.0
        )

        val userActivityEntity = ActivityInformationtoUserActivityEntity(activityInformation, true)

        assertEquals(1, userActivityEntity.activityID)
        assertEquals("Running", userActivityEntity.activityName)
        assertEquals(10.0, userActivityEntity.maxRain, 0.0)
        assertEquals(30.0, userActivityEntity.maxTemp, 0.0)
        assertEquals(5.0, userActivityEntity.maxWind, 0.0)
        assertEquals(0.0, userActivityEntity.minRain, 0.0)
        assertEquals(15.0, userActivityEntity.minTemp, 0.0)
        assertEquals(0.0, userActivityEntity.minWind, 0.0)
        assertEquals(true, userActivityEntity.selected)
    }

    @Test
    fun convertsActivityInformationToUserActivityEntityWithSelectedFalse() {
        val activityInformation = ActivityInformation(
            ActivityID = 2,
            ActivityName = "Swimming",
            MaxRain = 20.0,
            MaxTemp = 35.0,
            MaxWind = 10.0,
            MinRain = 5.0,
            MinTemp = 20.0,
            MinWind = 2.0
        )

        val userActivityEntity = ActivityInformationtoUserActivityEntity(activityInformation, false)

        assertEquals(2, userActivityEntity.activityID)
        assertEquals("Swimming", userActivityEntity.activityName)
        assertEquals(20.0, userActivityEntity.maxRain, 0.0)
        assertEquals(35.0, userActivityEntity.maxTemp, 0.0)
        assertEquals(10.0, userActivityEntity.maxWind, 0.0)
        assertEquals(5.0, userActivityEntity.minRain, 0.0)
        assertEquals(20.0, userActivityEntity.minTemp, 0.0)
        assertEquals(2.0, userActivityEntity.minWind, 0.0)
        assertEquals(false, userActivityEntity.selected)
    }
}
