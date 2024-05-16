package no.uio.ifi.in2000.team53.weatherapp.ui.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team53.weatherapp.R
import org.osmdroid.util.GeoPoint

/**
 * Composable for the Location screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [LocationViewModel] for handling the screen's logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    navController: NavController,
    viewModel: LocationViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val currentAddress by viewModel.currentAddress.collectAsState()
    var address by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.select_location)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.storeLocation()
                    navController.navigate("settings")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.current_location, currentAddress),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.enter_location)) },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.searchLocation(address)
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(searchResults) { result ->
                    ListItem(
                        text = result.getAddressLine(0),
                        onClick = {
                            val location = GeoPoint(result.latitude, result.longitude)
                            viewModel.updateLocation(location)
                            address = result.getAddressLine(0)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable for a list item.
 *
 * @param text The text to display in the list item.
 * @param onClick The action to perform when the list item is clicked.
 */
@Composable
fun ListItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}
