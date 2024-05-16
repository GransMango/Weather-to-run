
package no.uio.ifi.in2000.team53.weatherapp.ui.home
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import no.uio.ifi.in2000.team53.weatherapp.model.remote.metapis.ForecastTimeStep
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar
import no.uio.ifi.in2000.team53.weatherapp.utilities.formatIsoDateTime
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.pollenvarsel.PollenData

/**
 * Provides composables for the home screen of the weather app.
 *
 * @param navController The nav controller for navigating between screens.
 * @param viewModel The view model for the home screen.
 */
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val greetingText by viewModel.greetingText.collectAsState()
    val weatherIcons: MutableMap<String, Int> = viewModel.weatherIcons.forecastSummaryToDrawable
    val userActivitiesState by viewModel.userActivitiesState.collectAsState()


    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        DashboardScreen(navController, uiState, greetingText, userActivitiesState, weatherIcons)
    }
    Row(verticalAlignment = Alignment.Bottom) {
        BottomNavigationBar(navController)
    }
}

/**
 * Composable function for displaying the dashboard screen of the weather app.
 *
 * @param navController The nav controller for navigating between screens.
 * @param uiState The UI state of the home screen.
 * @param greetingText The greeting text displayed on the screen.
 * @param userActivities The list of user activities.
 * @param weatherIcons The mapping of weather conditions to drawable resources.
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    uiState: HomeUIState,
    greetingText: String,
    userActivities: List<UserActivityEntity>,
    weatherIcons: MutableMap<String, Int>
) {
    val cardHeight = 200.dp // Set a consistent height for all cards

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
    ) {
        item {
            Header(greetingText = greetingText)
        }
        item {
            PollenCard(height = cardHeight, pollenData = uiState.pollenData)
        }
        item {
            WeatherCard(uiState = uiState, height = 200.dp, weatherIcons = weatherIcons)
        }
        item {
            ActivitiesCard(navController = navController, userActivitiesState = userActivities, height = 100.dp)
        }
        item {
            RecommendedActivitiesCard(
                navController = navController,
                recommendedActivities = uiState.recommendedActivities.sportsIcons
            )
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Button(onClick = {
                navController.navigate("dailyoverview")
            }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.daily_overview))
            }
        }
    }
}

/**
 * Composable function for displaying the header section of the dashboard screen.
 *
 * @param greetingText The greeting text displayed on the screen.
 */

@Composable
fun Header(greetingText: String) {
    Spacer(modifier = Modifier.height(40.dp)) // Adjust height as needed
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = greetingText,
            fontSize = 25.sp
        )
        Lottie()
    }
    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth(0.8f))
    }
}

/**
 * Composable function for displaying the pollen card on the dashboard screen.
 *
 * @param height The height of the card.
 * @param pollenData The pollen data to be displayed.
 */
@Composable
fun PollenCard(height: Dp, pollenData: PollenData?) {
    var isLoading by remember { mutableStateOf(true) }

    Text(
        text = "Pollen",
        fontSize = 22.sp,
        modifier = Modifier.padding(start = 15.dp, top = 35.dp, bottom = 20.dp)
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(height), // Ensure the height is based on the content
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (isLoading) {
                    Text(
                        text = stringResource(R.string.loadingv2),
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    if (pollenData != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pollenData.displayName,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end = 15.dp)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.sneeze),
                                contentDescription = "Pollen Image",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                        Text(
                            text = pollenData.textForecast,
                            fontSize = 15.sp
                        )

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.naaf_logo),
                                contentDescription = "Naaf logo",
                                modifier = Modifier.size(200.dp),
                            )
                        }
                    } else {
                        // Display error message only when pollenData is null and not loading
                        Text(
                            text = stringResource(R.string.pollen_loadingerror),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    // Simulate data loading
    LaunchedEffect(Unit) {
        delay(2000) // Simulate a delay of 2 seconds
        isLoading = false
    }
}

/**
 * Composable function for displaying the weather card on the dashboard screen.
 *
 * @param uiState The UI state of the home screen.
 * @param height The height of the card.
 * @param weatherIcons The mapping of weather conditions to drawable resources.
 */
@Composable
fun WeatherCard(uiState: HomeUIState, height: Dp, weatherIcons: MutableMap<String, Int>) {
    val dateText = if (uiState.forecasts.isNotEmpty()) {
        val formattedDateTime = formatIsoDateTime(uiState.forecasts.first().time)
        val datePart = formattedDateTime.split(",").firstOrNull() ?: "Unknown Date"
        stringResource(R.string.today, datePart)
    } else {
        stringResource(R.string.todayv2)
    }

    Text(
        text = dateText,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 15.dp, top = 20.dp, bottom = 5.dp)
    )

    if (uiState.forecasts.isEmpty()) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp) // Ensure same horizontal padding
                .fillMaxWidth()
                .height(height),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.loadingv2),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else {
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(uiState.forecasts.drop(3).take(3)) { forecast ->
                ForecastCard(
                    forecast = forecast,
                    weatherIcons = weatherIcons,
                    height = height // Use the same height as the loading state
                )
            }
        }
    }
}

/**
 * Composable function for displaying the activities card on the dashboard screen.
 *
 * @param navController The nav controller for navigating between screens.
 * @param userActivitiesState The list of user activities.
 * @param height The height of the card.
 */
@Composable
fun ActivitiesCard(navController: NavController, userActivitiesState: List<UserActivityEntity>, height: Dp) {
    Text(
        text = stringResource(R.string.my_activities_profile),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
    )
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(userActivitiesState) { activity ->
            ActivityCard(navController = navController, activity = activity, height = height)
        }
    }
}

/**
 * Composable function for displaying the recommended activities card on the dashboard screen.
 *
 * @param recommendedActivities The list of recommended activities.
 */
@Composable
fun RecommendedActivitiesCard(navController: NavController, recommendedActivities: List<Int>) {
    Text(
        text = stringResource(R.string.explore_recommendations),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
    )
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(recommendedActivities) { drawableId ->
            Card(
                modifier = Modifier
                    .size(60.dp) // Adjust size as needed
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick =  {
                    navController.navigate("exploreactivities")
                }
            ) {
                Image(
                    painter = painterResource(drawableId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp) // Add some padding for the image
                        .fillMaxSize() // Ensure the image fills the Card
                )
            }
        }
    }
}

/**
 * Composable function for displaying a Lottie animation.
 */
@Composable
fun Lottie() {
    // change the animation down the line to something more relevant??
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.training3))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

/**
 * Composable function for displaying a forecast card.
 *
 * @param forecast The forecast data to be displayed.
 * @param weatherIcons The mapping of weather conditions to drawable resources.
 * @param height The height of the card.
 */
@Composable
fun ForecastCard(forecast: ForecastTimeStep?, weatherIcons: MutableMap<String, Int>, height: Dp) {
    if (forecast == null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("Trouble fetching weather data", Modifier.padding(16.dp))
            }
        }
    } else {
        val formattedDateTime = formatIsoDateTime(forecast.time)
        val parts = formattedDateTime.split(",")
        val time = parts.lastOrNull()?.trim()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = time ?: "",
                    fontSize = 18.sp
                )
                Row(
                    modifier = Modifier
                        .padding(top = 13.dp, end = 40.dp)
                        .fillMaxWidth()
                ) {
                    forecast.data.instant?.details?.let { details ->
                        Column {
                            WeatherDetail(
                                stringResource(id = R.string.temperature),
                                "${details.airTemperature}°C"
                            )
                            WeatherDetail(
                                stringResource(id = R.string.wind_speed),
                                "${details.windSpeed} m/s"
                            )
                            WeatherDetail(
                                stringResource(id = R.string.humidity),
                                "${details.relativeHumidity}%"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    forecast.data.next12Hours?.summary?.let { summary ->
                        val drawableResource = getDrawableResource(weatherIcons, summary.symbolCode)
                        Image(
                            painter = painterResource(id = drawableResource),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }
}


/**
 * Composable function for displaying an activity card.
 *
 * @param navController The nav controller for navigating between screens.
 * @param activity The user activity to be displayed.
 * @param height The height of the card.
 */
@Composable
fun ActivityCard(navController: NavController, activity: UserActivityEntity, height : Dp) {
    Card(
        onClick = {
            if (navController.currentDestination?.route != "UserDefinedActivityValues") {
                navController.navigate("UserDefinedActivityValues/${activity.activityName}")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(height), // Ensure the height is based on the content
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.White),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = activity.activityName, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = getDrawableResourceId(activity.activityName)),
                    contentDescription = activity.activityName,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
    }
}

/**
 * Composable function for displaying weather details.
 *
 * @param label The label of the weather detail.
 * @param value The value of the weather detail.
 */
@Composable
fun WeatherDetail(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(text = "$label:", fontSize = 15.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontSize = 15.sp)
    }
}


/** Function for manipulating the current route of the app.
 *
 * @param navController The nav controller for navigating between screens.
 * @return The current route of the app.
 */
@Composable
fun currentRoute(navController: NavController): String? {
    val currentRoute = remember(navController) {
        navController.currentBackStackEntry?.destination?.route
    }
    return currentRoute
}

/**
 * Retrieves the drawable resource ID based on the symbol code.
 *
 * @param forecastSummaryToDrawable The mapping of forecast summaries to drawable resources.
 * @param symbolCode The symbol code of the forecast.
 * @return The drawable resource ID.
 */
@Composable
fun getDrawableResource(forecastSummaryToDrawable: Map<String, Int>, symbolCode: String?): Int {
    // Retrieve the drawable resource based on the symbol code
    return forecastSummaryToDrawable[symbolCode] ?: R.drawable.cloudy
}

/**
 * Retrieves the drawable resource ID based on the user activity.
 *
 * @param activityName The name of the user activity.
 * @return The drawable resource ID.
 */
@Composable
fun getDrawableResourceId(activityName: String): Int {
    return when (activityName) {
        "Football" -> R.drawable.sports_soccer
        "Tennis" -> R.drawable.sports_tennis
        "Running" -> R.drawable.sports_running
        "Swimming" -> R.drawable.sports_swimming
        "Sailing" -> R.drawable.sports_sailing

        // Add more cases as needed
        else -> R.drawable.load // Placeholder drawable if activity name not found
    }
}