package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar

/**
 * Composable for the User Activities screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [UserActivitiesViewModel] for handling the screen's logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserActivities(
    navController: NavController,
    viewModel: UserActivitiesViewModel = hiltViewModel()
) {
    val userActivities by viewModel.userActivitiesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.my_activities_profile)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .fillMaxSize()

        ) {
            item {
                Spacer(modifier = Modifier
                    .size(60.dp)
                    .fillMaxWidth()) // Added spacers at the top and bottom of LazyColumn as it was getting overlapped by top and bottom bars of scaffold.
                Text(stringResource(R.string.my_activities_profile))
            }
            if (userActivities.isEmpty()) {
                item {
                    Text(stringResource(R.string.no_activities_chosen), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                items(userActivities) { activity ->
                    ActivityCard(activity, navController)
                }
            }
            item {
                Button(
                    onClick = { navController.navigate("exploreactivities") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.explore_more_activities), modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

/**
 * Composable for a user activity card.
 *
 * @param activity UserActivityEntity to display.
 * @param navController NavController for navigating between destinations.
 */
@Composable
fun ActivityCard(
    activity: UserActivityEntity, navController : NavController
) {
    Card(
        onClick = {
            if (navController.currentDestination?.route != "UserDefinedActivityValues") {
                navController.navigate("UserDefinedActivityValues/${activity.activityName}")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
            {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity.activityName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}