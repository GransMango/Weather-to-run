package no.uio.ifi.in2000.team53.weatherapp.utilities

import org.osmdroid.util.GeoPoint

/**
 * Converts a [GeoPoint] to a string.
 * @param geoPoint The [GeoPoint] to convert.
 *
 * @return The converted string.
 */
fun geoPointToString(geoPoint: GeoPoint): String {
    return "${geoPoint.latitude},${geoPoint.longitude}"
}

/**
 * Converts a string to a [GeoPoint].
 * @param location The string to convert.
 *
 * @return The converted [GeoPoint].
 */
fun stringToGeoPoint(location: String): GeoPoint {
    val parts = location.split(",")
    return GeoPoint(parts[0].toDouble(), parts[1].toDouble())
}