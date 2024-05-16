package no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi
import no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis.FeatureResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.team53.weatherapp.exceptions.AuthenticationException
import no.uio.ifi.in2000.team53.weatherapp.exceptions.AuthorizationException
import no.uio.ifi.in2000.team53.weatherapp.exceptions.BadRequestException
import no.uio.ifi.in2000.team53.weatherapp.exceptions.ResourceNotFoundException

/**
 * Provides access to the MET API for fetching weather data.
 * This class handles network operations to retrieve weather data from specific endpoints.
 * Includes handling of HTTP status codes and corresponding custom exceptions.
 */
class MetApiDatasource {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        defaultRequest {
            url(" https://api.met.no/")
            header("Accept", "application/json")
        }
    }

    /**
     * Fetches weather data from the MET API.
     * Parses and returns the response as a [FeatureResponse].
     *
     * @return A [FeatureResponse] instance representing the weather data.
     * @throws AuthenticationException if unauthorized access occurs due to API key issues.
     * @throws AuthorizationException if access is forbidden.
     * @throws ResourceNotFoundException if the endpoint URL is incorrect.
     * @throws BadRequestException if the request is malformed.
     * @throws Exception for all other types of HTTP errors.
     */
    suspend fun getWeather() : FeatureResponse {
        val response = client.get("weatherapi/locationforecast/2.0/edr/collections/compact/position?coords=POINT(10+60)&z=123")
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            else -> throw Exception("Error fetching weather: ${response.status}")
        }
    }

    /**
     * Fetches weather data from the MET API for a specific location.
     * Parses and returns the response as a [FeatureResponse].
     *
     * @param latitude the latitude of the location for which weather data is requested.
     * @param longitude the longitude of the location for which weather data is requested.
     * @return A [FeatureResponse] instance representing the weather data.
     * @throws AuthenticationException if unauthorized access occurs due to API key issues.
     * @throws AuthorizationException if access is forbidden.
     * @throws ResourceNotFoundException if the endpoint URL is incorrect.
     * @throws BadRequestException if the request is malformed.
     * @throws Exception for all other types of HTTP errors.
     */
    suspend fun getWeather(latitude: Double, longitude: Double) : FeatureResponse {
        val response = client.get("weatherapi/locationforecast/2.0/edr/collections/compact/position?coords=POINT($longitude+$latitude)&z=123")
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            HttpStatusCode.BadRequest -> throw BadRequestException("Bad request. Check the URL, specifically the POINT(lon+lat) parameter.")
            else -> throw Exception("Error fetching weather: ${response.status}")
        }
    }
}