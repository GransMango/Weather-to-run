package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar

/**
 * Composable for the Explore Activities screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [ExploreActivitiesViewModel] for handling the screen's logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreActivitiesScreen(
    navController: NavController,
    viewModel: ExploreActivitiesViewModel = hiltViewModel()
) {
    val activitiesState by viewModel.remoteActivities.collectAsState()
    val selectedOptions by viewModel.selectedActivities.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Activities") },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                if (activitiesState.isEmpty()) {
                    item {
                        Text(
                            "No activities available.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(activitiesState) { activity ->
                        RemoteActivityCard(activity, viewModel::addSelection, selectedOptions.contains(activity))
                    }
                }
            }
            Button(
                onClick = { viewModel.updateUserSelection(selectedOptions.map { it.ActivityID }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Confirm")
            }
        }
    }
}

/**
 * Composable for a card displaying remote activity information.
 *
 * @param activity [ActivityInformation] object representing the activity.
 * @param onSelect Function for selecting the activity.
 * @param isSelected Boolean representing whether the activity is selected.
 */
@Composable
fun RemoteActivityCard(
    activity: ActivityInformation,
    onSelect: (ActivityInformation) -> Unit,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity.ActivityName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect(activity) }
            )
        }
    }
}
