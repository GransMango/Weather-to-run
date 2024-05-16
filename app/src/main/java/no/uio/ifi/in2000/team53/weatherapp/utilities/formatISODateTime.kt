package no.uio.ifi.in2000.team53.weatherapp.utilities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Formats an ISO date time string to a more human-readable format.
 *
 * @param isoDateTime The ISO date time string to format.
 * @return The formatted date time string.
 */
fun formatIsoDateTime(isoDateTime: String): String {
    val parsedDate = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}