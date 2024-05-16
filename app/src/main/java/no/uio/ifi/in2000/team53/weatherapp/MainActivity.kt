package no.uio.ifi.in2000.team53.weatherapp

import no.uio.ifi.in2000.team53.weatherapp.ui.settings.SettingsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavItem
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.team53.weatherapp.ui.dailyoverview.DailyOverviewScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.home.HomeScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.home.UserDefinedActivityValuesScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.location.LocationScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.profile.ExploreActivitiesScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.profile.Profile
import no.uio.ifi.in2000.team53.weatherapp.ui.profile.UserActivities
import no.uio.ifi.in2000.team53.weatherapp.ui.profile.UserTimes
import no.uio.ifi.in2000.team53.weatherapp.ui.settings.AboutUsScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.setup.IntroductionScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.setup.NotificationScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.setup.SetupScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.setup.SetupTimesScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.setup.WelcomeScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.splash.SplashScreen
import no.uio.ifi.in2000.team53.weatherapp.ui.theme.WeatherAppTheme
import no.uio.ifi.in2000.team53.weatherapp.utilities.scheduleDaily8AMNotification

/**
 * The main activity of the application.
 * This activity is responsible for setting up the navigation graph and the notification.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    scheduleDaily8AMNotification(this@MainActivity) // Schedule the daily notification
                    AppNavigation(intent.getBooleanExtra("fromNotification", false))
                }
            }
        }
    }
}


/**
 * Composable for the navigation graph of the application.
 * This composable sets up the navigation graph for the application.
 */
@Composable
fun AppNavigation(fromNotification: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (fromNotification) "dailyoverview" else "splash"

    LaunchedEffect(fromNotification) {
        if (fromNotification) {
            navController.navigate("dailyoverview") {
                // Clear back stack to prevent going back to the splash screen
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") { SplashScreen(navController) }
        composable("setup") { SetupScreen(navController) }
        composable(BottomNavItem.Home.route) { HomeScreen(navController) }
        composable(BottomNavItem.Profile.route) { Profile(navController) }
        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }
        composable("UserActivities") { UserActivities(navController) }
        composable("UserTimes") { UserTimes(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("introduction") { IntroductionScreen(navController) }
        composable("setuptimesscreen") { SetupTimesScreen(navController) }
        composable("setupnotificationsscreen") { NotificationScreen(navController) }
        composable("about") { AboutUsScreen(navController) }
        composable("exploreactivities") { ExploreActivitiesScreen(navController) }
        composable("location") { LocationScreen(navController) }
        composable("dailyoverview") { DailyOverviewScreen(navController) }
        composable("UserDefinedActivityValues/{activityID}") { backStackEntry ->
            val activityName = backStackEntry.arguments?.getString("activityID")
            UserDefinedActivityValuesScreen(navController, activityName)
        }
    }
}

