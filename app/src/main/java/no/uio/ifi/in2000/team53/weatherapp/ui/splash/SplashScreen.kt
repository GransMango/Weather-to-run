package no.uio.ifi.in2000.team53.weatherapp.ui.splash

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.data.local.LocaleManager

/**
 * Composable for the splash screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param splashViewModel [SplashViewModel] for handling the screen's logic.
 */
@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel = hiltViewModel()) {
    val navigateTo by splashViewModel.navigateTo.collectAsState(initial = null)

    Log.d(splashViewModel.startupLanguage, "Startup Language: ${splashViewModel.startupLanguage}")
    if (splashViewModel.startupLanguage == "Norsk") LocaleManager.setLocale(LocalContext.current,"nb") //In other cases the app defaults to english.
    // Trigger navigation when navigateTo changes and is not null
    LaunchedEffect(navigateTo) {
        navigateTo?.let { screen ->
            navController.navigate(screen) {
                // I think this removes splash screen from navigation stack, so back button doesn't go back to it
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.loadinglogojokes)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "Weather App Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}
