package no.uio.ifi.in2000.team53.weatherapp.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Composable for the About Us screen.
 *
 * @param navController NavController for navigating between destinations.
 */
@Composable
fun AboutUsScreen(navController: NavController) {
    val context = LocalContext.current  // Get the local context to use for intents

    Box(modifier = Modifier.fillMaxSize()) {
        AboutBackground()

        Box(modifier = Modifier
            .matchParentSize()
            .background(Color.Black.copy(alpha = 0.3f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.about_us),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.about_us_description),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    val uri = Uri.parse("https://in2000-team53-documentation.azurewebsites.net")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = stringResource(R.string.view_more),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.home),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Composable for the background animation of the About Us screen.
 */
@Composable
fun AboutBackground() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.aboutusscreen))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 0.1f
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop)
}
