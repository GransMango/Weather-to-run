package no.uio.ifi.in2000.team53.weatherapp.ui.dailyoverview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.ui.home.DailyOverviewViewModel
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Composable for the daily overview screen.
 * This composable displays a greeting header and a list of recommended activities.
 * The recommended activities are based on the weather.
 * @param navController The navigation controller.
 * @param viewModel The view model for the daily overview screen.
 */
@Composable
fun DailyOverviewScreen(navController: NavController, viewModel: DailyOverviewViewModel = hiltViewModel()) {
    val recommendedActivities = viewModel.recommendedActivities.collectAsState(initial = emptyList())
    Box(modifier = Modifier.fillMaxWidth()) {
        SetupTimesBackground()

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            HorizontalDivider()

            GreetingHeader()
            if (recommendedActivities.value.isEmpty()) {
                Text(stringResource(R.string.no_activities),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally))
            } else {
                ActivitiesList(activities = recommendedActivities.value)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_home))
            }
        }
    }

}

/**
 * Composable for the greeting header.
 * This composable displays a greeting header.
 */
@Composable
fun GreetingHeader() {
    Text(stringResource(R.string.greeting_header),
        style = MaterialTheme.typography.bodyLarge,
        fontSize = 40.sp,
        modifier = Modifier.padding(16.dp))
}

/**
 * Composable for the list of recommended activities.
 * This composable displays a list of recommended activities.
 *
 * @param activities The list of recommended activities.
 */
@Composable
fun ActivitiesList(activities: List<UserActivityEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(activities) { activity ->
            ActivityCard(activity)
        }
    }
}

/**
 * Composable for an activity card.
 * This composable displays a card with a suggestion for an activity.
 *
 * @param activity The activity to display.
 */
@Composable
fun ActivityCard(activity: UserActivityEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val suggestions = listOf(
                stringResource(R.string.suggestion_great_day, activity.activityName),
                stringResource(R.string.suggestion_perfect_time, activity.activityName),
                stringResource(R.string.suggestion_enjoy_day, activity.activityName),
                stringResource(R.string.suggestion_have_fun, activity.activityName),
                stringResource(R.string.suggestion_great_day, activity.activityName),
                stringResource(R.string.suggestion_awesome_day, activity.activityName)
            )
            val suggestion = suggestions.random()
            Text(
                text = suggestion,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Composable for the background animation of the setup times screen.
 * This composable displays a Lottie animation in the background of the setup times screen.
 */
@Composable
fun SetupTimesBackground() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dailyoverviewscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f),
        contentScale = ContentScale.FillBounds
    )
}
