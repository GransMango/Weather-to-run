package no.uio.ifi.in2000.team53.weatherapp.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.data.local.LocaleManager
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar

/**
 * Composable for the Settings screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [SettingsViewModel] for handling the screen's logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val settingsState by viewModel.settingsState.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (showResetDialog, setShowResetDialog) = remember {mutableStateOf(false)}
    val availableLanguages = viewModel.getAvailableLanguages()
    val context = LocalContext.current

    //If the settings title is down below in the TopAppBar, recomposition is not triggered when we change language. So it can chill up here instead.
    val sTitle = stringResource(id = R.string.settings_title)
    //Same with buttons at the bottom
    val resetTitle = stringResource(R.string.reset_app)
    val aboutTitle = stringResource(R.string.about_us)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(sTitle, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                ClickableSettingItem(
                    title = stringResource(R.string.janky_language_fix),
                    onClick = { setShowDialog(true) }
                )
                if (showDialog) {
                    LanguageSelectionDialog(
                        availableLanguages = availableLanguages,
                        currentLanguage = settingsState.language,
                        onDismiss = { setShowDialog(false) },
                        onLanguageSelected = { language ->
                            viewModel.updateLanguage(language)
                            val newLanguage = viewModel.getNewLanguage()
                            LocaleManager.setLocale(context, newLanguage)
                            setShowDialog(false)
                        }
                    )
                }

                HorizontalDivider()

                ClickableSettingItem(
                    title = stringResource(R.string.set_location),
                    onClick = { navController.navigate("location") }
                )

                Button(
                    onClick = { setShowResetDialog(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        resetTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
                if (showResetDialog) {
                    ResetAppDialog(
                        onDismiss = { setShowResetDialog(false) },
                        viewModel
                    )
                }
                // About Us Button placed absolutely at the bottom
                Button(
                    onClick = { navController.navigate("about") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        aboutTitle,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * Composable component for generating a Clickable Setting Item.
 * @param title String representing the title of the setting.
 * @param onClick Lambda for handling the click event.
 */

@Composable
fun ClickableSettingItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = stringResource(R.string.go_to_setting))
    }
}

/**
 * Composable component for creating a language selection dialog.
 * @param currentLanguage String representing the current language.
 * @param onDismiss Lambda for dismissing the dialog.
 * @param onLanguageSelected Lambda for selecting a language.
 */
@Composable
fun LanguageSelectionDialog(availableLanguages: List<String>,currentLanguage: String, onDismiss: () -> Unit, onLanguageSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_language), style = MaterialTheme.typography.bodyLarge) },
        text = {
            Column {
                availableLanguages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(language)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == currentLanguage,
                            onClick = { onLanguageSelected(language) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = language,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), style = MaterialTheme.typography.bodyLarge)
            }
        }
    )
}

/**
 * Composable component for creating an app reset dialog.
 * @param onDismiss Lambda for dismissing the dialog.
 * @param viewModel for handling logic
 */
@Composable
fun ResetAppDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel){
    val coroutineScope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDismiss ,
        text = { Text(stringResource(R.string.are_you_sure_you_want_to_reset_the_app)) },
        confirmButton = {
            TextButton(onClick = onDismiss
        ) {
            Text(stringResource(R.string.no))
            }
        },
        dismissButton = {
            Button(onClick = {
                // Reset app code , clear/migrate db and stuff
                coroutineScope.launch{
                    viewModel.resetAppAndShutdown()
                }
                //No need to close this dialog here as the app closes anyway.
            }){
                Text(stringResource(R.string.yes))
            }
        }

    )
}

