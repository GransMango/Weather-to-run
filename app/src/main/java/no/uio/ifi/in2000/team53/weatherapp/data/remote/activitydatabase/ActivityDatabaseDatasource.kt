package no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.team53.weatherapp.exceptions.AuthenticationException
import no.uio.ifi.in2000.team53.weatherapp.exceptions.AuthorizationException
import no.uio.ifi.in2000.team53.weatherapp.exceptions.ResourceNotFoundException
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation

/**
 * Provides access to the remote activity database using HTTP requests.
 * Handles API communication and error management for activity-related data fetching.
 */
class ActivityDatabaseDatasource {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    /**
     * Fetches a list of activities from the remote database.
     * @return A list of [ActivityInformation] retrieved from the API.
     * @throws AuthenticationException if the API key is missing or incorrect.
     * @throws AuthorizationException if access to the resource is forbidden.
     * @throws ResourceNotFoundException if the resource URL is not found.
     * @throws Exception for other types of HTTP errors or internal server issues.
     */
    suspend fun getActivities() : List<ActivityInformation> {
        val response = client.get("https://in2000-weather-sqlapi.azurewebsites.net/api/activities")
        Log.d("Response", response.toString())
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            HttpStatusCode.InternalServerError -> throw Exception("Internal server error. Send pm til Daniel")
            else -> throw Exception("Error fetching weather: ${response.status}")
        }
    }

    /**
     * Fetches detailed information about a specific activity identified by [activity] ID.
     * @param activity The ID of the activity to fetch.
     * @return [ActivityInformation] containing details about the specific activity.
     * @throws AuthenticationException if the API key is missing or incorrect.
     * @throws AuthorizationException if access to the resource is forbidden.
     * @throws ResourceNotFoundException if the resource URL is not found.
     * @throws Exception for other types of HTTP errors or internal server issues.
     */
    suspend fun getActivity(activity : Int) : ActivityInformation {
        val response = client.get("https://in2000-weather-sqlapi.azurewebsites.net/api/activities/$activity")
        Log.d("Response", response.toString())
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthenticationException("Unauthorized access. Check API key setup in readme.")
            HttpStatusCode.Forbidden -> throw AuthorizationException("Forbidden access. You dont have access to the resource.")
            HttpStatusCode.NotFound -> throw ResourceNotFoundException("Resource not found. Check the URL.")
            else -> throw Exception("Error fetching weather: ${response.status}")
        }
    }



}