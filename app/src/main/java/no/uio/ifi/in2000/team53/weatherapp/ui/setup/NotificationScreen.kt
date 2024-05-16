package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Composable for the Notification screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [SetupViewModel] for handling the screen's logic.
 */
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.preferences.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(false) }
    var locationEnabled by remember { mutableStateOf(false) }
    var pollenNotificationsEnabled by remember { mutableStateOf(userPreferences.pollenNotification) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationEnabled = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NotificationBackground()

        Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.2f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.setup_notifications_title),
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.setup_notifications_description),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.celebration_message),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SwitchRow(
                label = stringResource(R.string.enable_notifications),
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )

            SwitchRow(
                label = stringResource(R.string.enable_location),
                checked = locationEnabled,
                onCheckedChange = { locationEnabled = it }
            )

            SwitchRow(
                label = stringResource(R.string.enable_pollen_notifications),
                checked = pollenNotificationsEnabled,
                onCheckedChange = {
                    pollenNotificationsEnabled = it
                    viewModel.updatePollenNotification(it)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val permissionsToRequest = mutableListOf<String>()
                    if (locationEnabled) {
                        permissionsToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        permissionsToRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                    if (notificationsEnabled) {
                        // If tiramisu api, add the permission
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionsToRequest.add(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && permissionsToRequest.isNotEmpty()) {
                        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
                    }
                    viewModel.updateIsFirstLaunch(false)
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.confirm_selections),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Composable for the Notification screen background animation.
 */
@Composable
fun NotificationBackground() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.notificationsscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/**
 * Composable for generating a row with a label and a switch.
 *
 * @param label String representing the label of the row.
 * @param checked Boolean representing the state of the switch.
 * @param onCheckedChange Lambda for handling the switch state change.
 */
@Composable
fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
