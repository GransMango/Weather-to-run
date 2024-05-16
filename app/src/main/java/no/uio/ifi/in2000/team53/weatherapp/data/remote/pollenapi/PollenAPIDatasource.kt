package no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi

import android.util.Log
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
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.PollenData
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.RegionPollenData

/**
 * Provides access to a remote API for fetching pollen-related data.
 * This class handles network operations to retrieve pollen data from specific endpoints.
 * Includes handling of HTTP status codes and corresponding custom exceptions.
 */
class PollenAPIDatasource {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        defaultRequest {
            url("https://in2000-reverseregionlookup.azurewebsites.net/")
            header("Accept", "application/json")
        }
    }


    /**
     * Fetches a list of pollen data regions from the remote service.
     * Parses and returns the response as a list of [PollenData].
     *
     * @return A list of [PollenData] instances representing the pollen data for various regions.
     * @throws AuthenticationException if unauthorized access occurs due to API key issues.
     * @throws AuthorizationException if access is forbidden.
     * @throws ResourceNotFoundException if the endpoint URL is incorrect.
     * @throws BadRequestException if the request is malformed.
     * @throws Exception for all other types of HTTP errors.
     */
    suspend fun getPollenRegions() : List<PollenData> {
        val response = client.get("/pollen/regions")
        Log.d("PollenAPIDatasource", "Response: $response")
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            HttpStatusCode.BadRequest -> throw BadRequestException("Bad request. Check URL.")
            else -> throw Exception("Error fetching pollen regions: ${response.status}")
        }
    }

    /**
     * Retrieves pollen data for a specific region identified by latitude and longitude coordinates.
     * Parses and returns the response as [RegionPollenData].
     *
     * @param lat Latitude of the region.
     * @param lon Longitude of the region.
     * @return [RegionPollenData] representing the pollen data for the specified coordinates.
     * @throws AuthenticationException if unauthorized access occurs due to API key issues.
     * @throws AuthorizationException if access is forbidden.
     * @throws ResourceNotFoundException if the endpoint URL is incorrect or the regionId is illegal.
     * @throws BadRequestException if the request parameters are incorrect.
     * @throws Exception for all other types of HTTP errors.
     */
    suspend fun getPollenDataForRegion(lat : Double, lon : Double) : RegionPollenData {
        val response = client.get("pollen?lat=$lat&lon=$lon")
        Log.d("PollenAPIDatasource", "Response: $response")
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            HttpStatusCode.BadRequest -> throw BadRequestException("Bad request. Check the URL, specifically if regionId is legal.")
            else -> throw Exception("Error fetching pollendata for lat $lat and lon $lon: ${response.status}")
        }
    }
}