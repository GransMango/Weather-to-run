package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import android.app.TimePickerDialog
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.*
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Composable for the Setup Times screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [SetupViewModel] for handling the screen's logic.
 */
@Composable
fun SetupTimesScreen(navController: NavController, viewModel: SetupViewModel = hiltViewModel()) {
    var startTime by remember { mutableStateOf<String?>(null) }
    var endTime by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        SetupTimesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.setup_times_title),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.setup_times_description),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimePickerSection("Start Time", startTime) { selectedTime ->
                startTime = selectedTime
            }

            TimePickerSection("End Time", endTime) { selectedTime ->
                endTime = selectedTime
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (startTime != null && endTime != null) {
                        viewModel.updateUserTimes(startTime!!, endTime!!)
                        navController.navigate("setupnotificationsscreen")
                    } else {
                        dialogMessage = if (startTime == null && endTime == null) {
                            "Both start and end times are not set."
                        } else if (startTime == null) {
                            "Start time is not set."
                        } else {
                            "End time is not set."
                        }
                        showDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp) // Added padding for aesthetic purposes
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.set_time_slots_button),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Missing Information", color = Color.Black) },
                    text = { Text(dialogMessage, color = Color.Black) },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

/**
 * Composable for the Time Picker section.
 *
 * @param label String representing the label of the section.
 * @param time String representing the time selected.
 * @param onTimeSelected Lambda for handling the selection of a time.

 */
@Composable
fun TimePickerSection(label: String, time: String?, onTimeSelected: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        TimePickerButton(label, time, onTimeSelected)
    }
}


/**
 * Composable for the Time Picker button.
 *
 * @param label String representing the label of the button.
 * @param time String representing the time selected.
 * @param onTimeSelected Lambda for handling the selection of a time.
 */
@Composable
fun TimePickerButton(label: String, time: String?, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = {
            val calendar = Calendar.getInstance()
            TimePickerDialog(context, { _, hourOfDay, minuteOfHour ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                onTimeSelected(formattedTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Text(text = "$label: ${time ?: "Not set"}", color = Color.White, fontSize = 16.sp)
    }
}

/**
 * Composable for the Setup Times screen background animation.
 */
@Composable
fun SetupTimesBackground() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.setuptimesscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}
