package no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis

import kotlinx.serialization.*

/**
 * Data class for storing location forecast information.
 *
 * @param geometry PointGeometry object containing the location's coordinates.
 * @param properties Forecast object containing the forecast information.
 * @param type String representing the type of the feature.
 */
@Serializable
data class FeatureResponse(
    val geometry: PointGeometry,
    val properties: Forecast,
    val type: String // Assuming 'Feature' is one of the values
)

/**
 * Data class for storing point geometry information.
 *
 * @param coordinates List of Doubles representing the location's coordinates.
 * @param type String representing the type of the geometry.
 */
@Serializable
data class PointGeometry(
    val coordinates: List<Double>, // [longitude, latitude, altitude]
    val type: String
)

/**
 * Data class for storing forecast information.
 *
 * @param meta InlineModel object containing the forecast's metadata.
 * @param timeseries List of ForecastTimeStep objects representing the forecast's time series.
 */
@Serializable
data class Forecast(
    val meta: InlineModel,
    val timeseries: List<ForecastTimeStep>
)

/**
 * Data class for storing inline model information.
 *
 * @param units ForecastUnits object containing the forecast's units.
 * @param updatedAt String representing the time the forecast was last updated.
 */
@Serializable
data class InlineModel(
    val units: ForecastUnits,
    @SerialName("updated_at") val updatedAt: String
)

/**
 * Data class for storing forecast units information.
 *
 * @param airPressureAtSeaLevel String representing the air pressure at sea level.
 * @param airTemperature String representing the air temperature.
 * @param airTemperatureMax String representing the maximum air temperature.
 * @param airTemperatureMin String representing the minimum air temperature.
 * @param cloudAreaFraction String representing the cloud area fraction.
 * @param cloudAreaFractionHigh String representing the high cloud area fraction.
 * @param cloudAreaFractionLow String representing the low cloud area fraction.
 * @param cloudAreaFractionMedium String representing the medium cloud area fraction.
 * @param dewPointTemperature String representing the dew point temperature.
 * @param fogAreaFraction String representing the fog area fraction.
 * @param precipitationAmount String representing the precipitation amount.
 * @param precipitationAmountMax String representing the maximum precipitation amount.
 * @param precipitationAmountMin String representing the minimum precipitation amount.
 * @param probabilityOfPrecipitation String representing the probability of precipitation.
 * @param probabilityOfThunder String representing the probability of thunder.
 * @param relativeHumidity String representing the relative humidity.
 * @param ultravioletIndexClearSkyMax String representing the maximum ultraviolet index.
 * @param windFromDirection String representing the wind direction.
 * @param windSpeed String representing the wind speed.
 * @param windSpeedOfGust String representing the wind gust speed.
 */
@Serializable
data class ForecastUnits(
    @SerialName("air_pressure_at_sea_level") val airPressureAtSeaLevel: String? = null,
    @SerialName("air_temperature") val airTemperature: String? = null,
    @SerialName("air_temperature_max") val airTemperatureMax: String? = null,
    @SerialName("air_temperature_min") val airTemperatureMin: String? = null,
    @SerialName("cloud_area_fraction") val cloudAreaFraction: String? = null,
    @SerialName("cloud_area_fraction_high") val cloudAreaFractionHigh: String? = null,
    @SerialName("cloud_area_fraction_low") val cloudAreaFractionLow: String? = null,
    @SerialName("cloud_area_fraction_medium") val cloudAreaFractionMedium: String? = null,
    @SerialName("dew_point_temperature") val dewPointTemperature: String? = null,
    @SerialName("fog_area_fraction") val fogAreaFraction: String? = null,
    @SerialName("precipitation_amount") val precipitationAmount: String? = null,
    @SerialName("precipitation_amount_max") val precipitationAmountMax: String? = null,
    @SerialName("precipitation_amount_min") val precipitationAmountMin: String? = null,
    @SerialName("probability_of_precipitation") val probabilityOfPrecipitation: String? = null,
    @SerialName("probability_of_thunder") val probabilityOfThunder: String? = null,
    @SerialName("relative_humidity") val relativeHumidity: String? = null,
    @SerialName("ultraviolet_index_clear_sky_max") val ultravioletIndexClearSkyMax: String? = null,
    @SerialName("wind_from_direction") val windFromDirection: String? = null,
    @SerialName("wind_speed") val windSpeed: String? = null,
    @SerialName("wind_speed_of_gust") val windSpeedOfGust: String? = null
)

/**
 * Data class for storing forecast time step information.
 *
 * @param data InlineModel0 object containing the forecast data.
 * @param time String representing the time of the forecast.
 */
@Serializable
data class ForecastTimeStep(
    val data: InlineModel0,
    val time: String
)

/**
 * Data class for storing inline model 0 information.
 *
 * @param instant InlineModel1 object containing the forecast's instant data.
 * @param next12Hours InlineModel2 object containing the forecast's next 12 hours data.
 * @param next1Hours InlineModel3 object containing the forecast's next 1 hour data.
 * @param next6Hours InlineModel4 object containing the forecast's next 6 hours data.
 */
@Serializable
data class InlineModel0(
    val instant: InlineModel1?,
    @SerialName("next_12_hours") val next12Hours: InlineModel2? = null,
    @SerialName("next_1_hours") val next1Hours: InlineModel3? = null,
    @SerialName("next_6_hours") val next6Hours: InlineModel4? = null
)

/**
 * Data class for storing inline model 1 information.
 *
 * @param details ForecastTimeInstant object containing the forecast's instant details.
 */
@Serializable
data class InlineModel1(
    val details: ForecastTimeInstant? = null
)

/**
 * Data class for storing inline model 2 information.
 *
 * @param details ForecastTimePeriod object containing the forecast's next 12 hours details.
 * @param summary ForecastSummary object containing the forecast's summary.
 */
@Serializable
data class InlineModel2(
    val details: ForecastTimePeriod?,
    val summary: ForecastSummary?
)

/**
 * Data class for storing inline model 3 information.
 *
 * @param details ForecastTimePeriod object containing the forecast's next 1 hour details.
 * @param summary ForecastSummary object containing the forecast's summary.
 */
@Serializable
data class InlineModel3(
    val details: ForecastTimePeriod?,
    val summary: ForecastSummary?
)

/**
 * Data class for storing inline model 4 information.
 *
 * @param details ForecastTimePeriod object containing the forecast's next 6 hours details.
 * @param summary ForecastSummary object containing the forecast's summary.
 */
@Serializable
data class InlineModel4(
    val details: ForecastTimePeriod?,
    val summary: ForecastSummary?
)

/**
 * Data class for storing forecast time instant information.
 *
 * @param airPressureAtSeaLevel Double representing the air pressure at sea level.
 * @param airTemperature Double representing the air temperature.
 * @param cloudAreaFraction Double representing the cloud area fraction.
 * @param cloudAreaFractionHigh Double representing the high cloud area fraction.
 * @param cloudAreaFractionLow Double representing the low cloud area fraction.
 * @param cloudAreaFractionMedium Double representing the medium cloud area fraction.
 * @param dewPointTemperature Double representing the dew point temperature.
 * @param fogAreaFraction Double representing the fog area fraction.
 * @param relativeHumidity Double representing the relative humidity.
 * @param windFromDirection Double representing the wind direction.
 * @param windSpeed Double representing the wind speed.
 * @param windSpeedOfGust Double representing the wind gust speed.
 */
@Serializable
data class ForecastTimeInstant(
    @SerialName("air_pressure_at_sea_level") val airPressureAtSeaLevel: Double? = null,
    @SerialName("air_temperature") val airTemperature: Double? = null,
    @SerialName("cloud_area_fraction") val cloudAreaFraction: Double? = null,
    @SerialName("cloud_area_fraction_high") val cloudAreaFractionHigh: Double? = null,
    @SerialName("cloud_area_fraction_low") val cloudAreaFractionLow: Double? = null,
    @SerialName("cloud_area_fraction_medium") val cloudAreaFractionMedium: Double? = null,
    @SerialName("dew_point_temperature") val dewPointTemperature: Double? = null,
    @SerialName("fog_area_fraction") val fogAreaFraction: Double? = null,
    @SerialName("relative_humidity") val relativeHumidity: Double? = null,
    @SerialName("wind_from_direction") val windFromDirection: Double? = null,
    @SerialName("wind_speed") val windSpeed: Double? = null,
    @SerialName("wind_speed_of_gust") val windSpeedOfGust: Double? = null
)

/**
 * Data class for storing forecast time period information.
 *
 * @param airTemperatureMax Double representing the maximum air temperature.
 * @param airTemperatureMin Double representing the minimum air temperature.
 * @param precipitationAmount Double representing the precipitation amount.
 * @param precipitationAmountMax Double representing the maximum precipitation amount.
 * @param precipitationAmountMin Double representing the minimum precipitation amount.
 * @param probabilityOfPrecipitation Double representing the probability of precipitation.
 * @param probabilityOfThunder Double representing the probability of thunder.
 * @param ultravioletIndexClearSkyMax Double representing the maximum ultraviolet index.
 */
@Serializable
data class ForecastTimePeriod(
    // Assuming these are the summary details for the period types (e.g., next_1_hours)
    @SerialName("air_temperature_max") val airTemperatureMax: Double? = null,
    @SerialName("air_temperature_min") val airTemperatureMin: Double? = null,
    @SerialName("precipitation_amount") val precipitationAmount: Double? = null,
    @SerialName("precipitation_amount_max") val precipitationAmountMax: Double? = null,
    @SerialName("precipitation_amount_min") val precipitationAmountMin: Double? = null,
    @SerialName("probability_of_precipitation") val probabilityOfPrecipitation: Double? = null,
    @SerialName("probability_of_thunder") val probabilityOfThunder: Double? = null,
    @SerialName("ultraviolet_index_clear_sky_max") val ultravioletIndexClearSkyMax: Double? = null
)

/**
 * Data class for storing forecast summary information.
 *
 * @param symbolCode String representing the symbol code.
 */
@Serializable
data class ForecastSummary(
    @SerialName("symbol_code") val symbolCode: String // Not marked as optional based on your model but change if needed
)
