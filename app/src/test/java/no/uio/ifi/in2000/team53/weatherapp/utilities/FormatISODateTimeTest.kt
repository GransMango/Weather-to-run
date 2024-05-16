package no.uio.ifi.in2000.team53.weatherapp.utilities

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FormatISODateTimeTest {

    @Test
    fun formatIsoDateTime_correctlyFormatsDateTime() {
        val isoDateTime = "2022-01-01T23:00:00"
        val expected = "1 Jan 2022, 23:00:00"
        val actual = formatIsoDateTime(isoDateTime)
        assertEquals(expected, actual)
    }

    @Test
    fun formatIsoDateTime_handlesIncorrectFormat() {
        val isoDateTime = "incorrect format"
        try {
            formatIsoDateTime(isoDateTime)
        } catch (e: Exception) {
            assertEquals(java.time.format.DateTimeParseException::class, e::class)
        }
    }

    @Test
    fun formatIsoDateTime_handlesEmptyString() {
        val isoDateTime = ""
        try {
            formatIsoDateTime(isoDateTime)
        } catch (e: Exception) {
            assertEquals(java.time.format.DateTimeParseException::class, e::class)
        }
    }
}