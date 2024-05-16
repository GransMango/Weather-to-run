package no.uio.ifi.in2000.team53.weatherapp.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation

/**
 * Composable for the Setup screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [SetupViewModel] for handling the screen's logic.

 */
@Composable
fun SetupScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val activitiesState by viewModel.activities.collectAsState()
    val selectedOptions by viewModel.selectedActivities.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        SetupBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionHeader()

            LazyColumn(modifier = Modifier.weight(1f)) {
                item { SectionTitle(stringResource(R.string.select_preferred_activities)) }
                item {
                    OptionsSection(
                        title = stringResource(R.string.available_options),
                        options = activitiesState - selectedOptions,
                        onOptionSelect = { viewModel.addSelection(it) }
                    )
                }
                item {
                    OptionsSection(
                        title = stringResource(R.string.selected_options),
                        options = selectedOptions,
                        onOptionSelect = { viewModel.removeSelection(it) }
                    )
                }
            }

            ConfirmSelectionsButton(selectedOptions, viewModel, navController)
        }
    }
}

/**
 * Composable for the setup background animation.
 */
@Composable
fun SetupBackground() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.backgroundsetup))
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
 * Composable component for the section header.
 */
@Composable
fun SectionHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(R.string.setup_notifications_title),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

/**
 * Composable component for the section title.
 *
 * @param text String representing the title of the section.
 */
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Composable component for the options section.
 *
 * @param title String representing the title of the section.
 * @param options List of [ActivityInformation] representing the options to display.
 * @param onOptionSelect Lambda for handling the selection of an option.
 */
@Composable
fun OptionsSection(
    title: String,
    options: List<ActivityInformation>,
    onOptionSelect: (ActivityInformation) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
        options.forEach { option ->
            OptionButton(option.ActivityName) { onOptionSelect(option) }
        }
    }
}

/**
 * Composable component for the confirm selections button.
 *
 * @param selectedOptions List of [ActivityInformation] representing the selected options.
 * @param viewModel [SetupViewModel] for handling the screen's logic.
 * @param navController NavController for navigating between destinations.
 */
@Composable
fun ConfirmSelectionsButton(
    selectedOptions: List<ActivityInformation>,
    viewModel: SetupViewModel,
    navController: NavController,
) {
    Button(
        onClick = {
            viewModel.updateUserSelection(selectedOptions.map { it.ActivityID })
            navController.navigate("setuptimesscreen")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(R.string.confirm_selections),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Composable component for the option button.
 *
 * @param option String representing the option to display.
 * @param onClick Lambda for handling the click event.
 */
@Composable
fun OptionButton(option: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(50)
    ) {
        Text(option, style = MaterialTheme.typography.bodyLarge)
    }
}
