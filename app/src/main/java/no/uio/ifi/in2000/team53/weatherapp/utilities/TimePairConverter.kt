package no.uio.ifi.in2000.team53.weatherapp.utilities

import androidx.room.TypeConverter

/**
 * Converts a pair of strings to a single string and vice versa.
 */
class TimePairConverter { //Needed type converter for pairs as room db does not support inserting pairs :(

    /**
     * Converts a pair of strings to a single string.
     * @param pair The pair of strings to convert.
     * @return The converted string.
     */
    @TypeConverter
    fun fromPair(pair: Pair<String, String>?): String? {
        return pair?.let { "${it.first},${it.second}" }
    }

    /**
     * Converts a single string to a pair of strings.
     * @param value The string to convert.
     * @return The converted pair.
     */
    @TypeConverter
    fun toPair(value: String?): Pair<String, String>? {
        return value?.split(",")?.let { Pair(it[0], it[1]) }
    }
}