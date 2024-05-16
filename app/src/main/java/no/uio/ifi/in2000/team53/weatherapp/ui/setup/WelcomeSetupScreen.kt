package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import isNetworkAvailable
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.data.local.LocaleManager
import no.uio.ifi.in2000.team53.weatherapp.ui.settings.ClickableSettingItem
import no.uio.ifi.in2000.team53.weatherapp.ui.settings.LanguageSelectionDialog
import no.uio.ifi.in2000.team53.weatherapp.ui.settings.SettingsViewModel

/**
 * Composable for the Welcome screen.
 *
 * @param navController NavController for navigating between destinations.
 */
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isNetworkAvailable = remember { mutableStateOf(true) }

    //For translation
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val buttonTranslation = stringResource(id = R.string.go_to_setup)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Lottie Animation as background
        Lottie()


        LaunchedEffect(key1 = true) {
            isNetworkAvailable.value = isNetworkAvailable(context)
        }

        if (!isNetworkAvailable.value) {
            NetworkAlertDialog(onDismiss = { isNetworkAvailable.value = true })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp)) // Top padding

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_to_our_weather_app),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp) // Adjust padding as needed
                )

                Image(
                    painter = painterResource(id = R.drawable.naaf_logo), // replace with your logo
                    contentDescription = stringResource(id = R.string.partner_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(400.dp) // Increase size as needed
                        .height(200.dp) // Increase size as needed
                )
                Row(
                    modifier = Modifier.background(Color (0x4DFFFFFF))
                ){
                    ClickableSettingItem(
                        title = stringResource(R.string.janky_language_fix),
                        onClick = { setShowDialog(true) }
                    )
                }

                if (showDialog) {
                    LanguageSelectionDialog(
                        availableLanguages = viewModel.getAvailableLanguages(),
                        currentLanguage = viewModel.getNewLanguage(),
                        onDismiss = { setShowDialog(false) },
                        onLanguageSelected = { language ->
                            viewModel.updateLanguage(language)
                            val newLanguage = viewModel.getNewLanguage()
                            LocaleManager.setLocale(context, newLanguage)
                            setShowDialog(false)
                        }
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("introduction") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = buttonTranslation,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.developed_with_naaf),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Composable to show an alert dialog when there is no internet connection.
 */
@Composable
fun NetworkAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("OK")
            }
        },
        title = { Text("Network Issue") },
        text = { Text("Internet should be on for the best experience.") }
    )
}

/**
 * Composable for the Lottie animation.
 */
@Composable
fun Lottie() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.welcomescreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        contentScale = ContentScale.Crop // Ensures the animation fills the screen by cropping
    )
}
