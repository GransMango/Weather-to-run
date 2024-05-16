package no.uio.ifi.in2000.team53.weatherapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar


/**
 * Composable function for displaying the user-defined activity values screen.
 * @param navController The navigation controller for navigating between destinations.
 * @param activityName The name of the activity.
 * @param viewModel The view model for handling user-defined activities.
 */

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState",
    "StateFlowValueCalledInComposition"
)
@Composable
fun UserDefinedActivityValuesScreen(
    navController: NavController, activityName: String?,
    viewModel: UserDefinedActivityValuesViewModel = hiltViewModel()
) {
    LaunchedEffect(activityName) {
        if (activityName != null) {
            viewModel.getFilteredActivityByName(activityName)
        }
    }

    val updateActivity: (UserActivityEntity) -> Unit = { updatedActivity ->
        viewModel.updateActivity(updatedActivity)
    }

    val deleteActivity: (UserActivityEntity) -> Unit = { activity ->
        viewModel.deleteActivity(activity)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mine Aktiviteter") },
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
                    .fillMaxWidth())
                Text("")
            }

            item {
                viewModel.filteredActivity.value?.let { activity ->
                    ActivityPreferences(
                        activity = activity,
                        onActivityUpdate = { updatedActivity ->
                            viewModel.updateActivity(updatedActivity)
                        },
                        onDeleteActivity = {
                            viewModel.deleteActivity(activity)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable function for displaying the activity preferences.
 * @param activity The user-defined activity entity.
 * @param onActivityUpdate The callback function to update the activity.
 * @param onDeleteActivity The callback function to delete the activity.
 */

@Composable
fun ActivityPreferences(
    activity: UserActivityEntity,
    onActivityUpdate: (UserActivityEntity) -> Unit,
    onDeleteActivity: () -> Unit  // Added this lambda for deletion
) {
    val (minRain, setMinRain) = remember { mutableStateOf(activity.minRain.toString()) }
    val (maxRain, setMaxRain) = remember { mutableStateOf(activity.maxRain.toString()) }
    val (minTemp, setMinTemp) = remember { mutableStateOf(activity.minTemp.toString()) }
    val (maxTemp, setMaxTemp) = remember { mutableStateOf(activity.maxTemp.toString()) }
    val (minWind, setMinWind) = remember { mutableStateOf(activity.minWind.toString()) }
    val (maxWind, setMaxWind) = remember { mutableStateOf(activity.maxWind.toString()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Preferences for ${activity.activityName}")
        Spacer(modifier = Modifier.height(16.dp))
        RangeInput("Rain", activity.minRain, activity.maxRain) { newMin, newMax ->
            setMinRain(newMin.toString())
            setMaxRain(newMax.toString())
        }
        Spacer(modifier = Modifier.height(16.dp))
        RangeInput("Temperature", activity.minTemp, activity.maxTemp) { newMin, newMax ->
            setMinTemp(newMin.toString())
            setMaxTemp(newMax.toString())
        }
        Spacer(modifier = Modifier.height(16.dp))
        RangeInput("Wind", activity.minWind, activity.maxWind) { newMin, newMax ->
            setMinWind(newMin.toString())
            setMaxWind(newMax.toString())
        }

        Button(
            onClick = {
                val updatedActivity = activity.copy(
                    minRain = minRain.toDoubleOrNull() ?: activity.minRain,
                    maxRain = maxRain.toDoubleOrNull() ?: activity.maxRain,
                    minTemp = minTemp.toDoubleOrNull() ?: activity.minTemp,
                    maxTemp = maxTemp.toDoubleOrNull() ?: activity.maxTemp,
                    minWind = minWind.toDoubleOrNull() ?: activity.minWind,
                    maxWind = maxWind.toDoubleOrNull() ?: activity.maxWind
                )
                onActivityUpdate(updatedActivity)
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text("Update")
        }

        Button(
            onClick = { onDeleteActivity() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Delete")
        }
    }
}

/**
 * Composable function for displaying a range input.
 * @param label The label for the range input.
 * @param minValue The minimum value of the range.
 * @param maxValue The maximum value of the range.
 * @param onRangeChange The callback function to handle changes in the range.
 */
@Composable
fun RangeInput(
    label: String,
    minValue: Double,
    maxValue: Double,
    onRangeChange: (Double, Double) -> Unit
) {
    var minText by remember { mutableStateOf(minValue.toString()) }
    var maxText by remember { mutableStateOf(maxValue.toString()) }

    Column {
        Text(text = label)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = minText,
                onValueChange = {
                    minText = it
                    onRangeChange(it.toDoubleOrNull() ?: minValue, maxText.toDoubleOrNull() ?: maxValue)
                },
                label = { Text("Min") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = maxText,
                onValueChange = {
                    maxText = it
                    onRangeChange(minText.toDoubleOrNull() ?: minValue, it.toDoubleOrNull() ?: maxValue)
                },
                label = { Text("Max") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
    }
}