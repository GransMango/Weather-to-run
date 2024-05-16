package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar
import java.util.Locale

/**
 * Composable for the User Times screen.
 * @param navController NavController for navigating between destinations.
 * @param viewModel [UserTimesViewModel] for handling the screen's logic.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimes(navController: NavController, viewModel: UserTimesViewModel = hiltViewModel()) {
    val userTimes by viewModel.userTimes.collectAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableIntStateOf(0) }
    val currentLanguage = Locale.getDefault().language
    val daysInNorwegian = listOf("Man","Tir","Ons","Tor","Fre","Lør","Søn") //Janky solution, should work for now

    viewModel.checkDaysInTable()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_times_profile)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            R.string.back_button
                        ))
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp))
            for (dayIndex in 0 until 7) {
                val day = getDayOfWeek(dayIndex)
                val userTimesForDay = userTimes.firstOrNull { it?.day == day }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if(currentLanguage == "en") day else daysInNorwegian[dayIndex],
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            showDialog = true
                            selectedDay = dayIndex
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_time_usertimes)
                        )
                    }
                    userTimesForDay?.listOfTimes?.let { interval ->
                        Text(
                            text = "${interval.first} - ${interval.second}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            if (showDialog) {
                AddTimeAlertDialog(
                    selectedDay = selectedDay,
                    onDialogDismiss = {
                        showDialog = false
                    },
                    onAddTime = { from, to ->
                        viewModel.addTimeForDay(getDayOfWeek(selectedDay), from to to)
                        showDialog = false
                    }
                )
            }
        }
    }
}

/**
 * Composable for an alert dialog to add a time interval for a day.
 * @param selectedDay The index of the selected day.
 * @param onDialogDismiss Lambda for dismissing the dialog.
 * @param onAddTime Lambda for adding the time interval.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeAlertDialog(
    selectedDay: Int,
    onDialogDismiss: () -> Unit,
    onAddTime: (String, String) -> Unit
) {
    var errorText by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf<String?>(null) }
    var endTime by remember { mutableStateOf<String?>(null) }

    BasicAlertDialog(
        onDismissRequest = onDialogDismiss,
        modifier = Modifier.padding(16.dp).background(color = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).background(color = Color.White)
        ) {
            Text(
                text = stringResource(
                    R.string.add_time_title_usertimes,
                    getDayOfWeek(selectedDay)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            //Re-using the TimePicker from the setup screen here
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                no.uio.ifi.in2000.team53.weatherapp.ui.setup.TimePickerSection("Start Time", startTime
                ) { selectedTime ->
                    startTime = selectedTime
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                no.uio.ifi.in2000.team53.weatherapp.ui.setup.TimePickerSection("End Time", endTime
                ) { selectedTime ->
                    endTime = selectedTime
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (errorText.isNotEmpty()) {
                Text(
                    text = errorText,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //Button to back out of time selection
                Button(
                    onClick = { onDialogDismiss() },
                    enabled = true
                ) {
                    Text(stringResource(R.string.back_button))
                }
                // Button to add the time interval for the selected day.
                Button(
                    onClick = {
                        if (startTime != null && endTime != null) {
                            onAddTime(startTime!!, endTime!!)
                        } else {
                            errorText = if (startTime == null && endTime == null) {
                                "Both start and end times are not set."
                            } else if (startTime == null) {
                                "Start time is not set."
                            } else {
                                "End time is not set."
                            }
                        }
                    },
                    enabled = true
                ) {
                    Text(stringResource(R.string.add_usertimes))
                }
            }
        }

    }
}

/**
 * Helper function for retrieving the correct weekday from a given integer
 *
 * @param day the index of the day
 * @return string value of needed day
 */
fun getDayOfWeek(day: Int): String {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    return daysOfWeek.getOrElse(day) { "" }
}
