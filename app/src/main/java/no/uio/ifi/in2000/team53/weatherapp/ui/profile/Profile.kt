package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import no.uio.ifi.in2000.team53.weatherapp.R
import no.uio.ifi.in2000.team53.weatherapp.ui.navigation.BottomNavigationBar

/**
 * Composable for the Profile screen.
 *
 * @param navController NavController for navigating between destinations.
 * @param viewModel [ProfileViewModel] for handling the screen's logic.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun Profile(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val name by viewModel.name.collectAsState("")
    //Log.d(name,"$name<----------- Name in profile page")
    val profilePictureUri by viewModel.profilePictureUri.collectAsState(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Profile picture and name
        ProfileHeader(name, profilePictureUri, viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            text = stringResource(R.string.my_activities_profile),
            onClick = { navController.navigate("UserActivities") }
        )
        ProfileMenuItem(
            text = stringResource(R.string.my_times_profile),
            onClick = { navController.navigate("UserTimes") }
        )
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            bottomBar = { BottomNavigationBar(navController = navController) }
        ){

        }
    }
}

/**
 * Composable for the profile menu items.
 *
 * @param name Name of the user.
 * @param profilePictureUri Uri of the profile picture.
 * @param viewModel [ProfileViewModel] for handling the screen's logic.
 */
@Composable
fun ProfileHeader(name: String, profilePictureUri: String?, viewModel: ProfileViewModel) {
    var nameState by remember { mutableStateOf(name)}
    LaunchedEffect(name) {//Name does not show without this for some reason
        nameState = name
    }
    //Log.d("ProfileHeader", "name: $name, nameState: $nameState, picUri: $profilePictureUri", )
    val keyboardController = LocalSoftwareKeyboardController.current
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            viewModel.setProfilePicture(uri.toString())
        }
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Profile picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Blue)
                .clickable { getContent.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            // Display the profile picture if available
            if (!profilePictureUri.isNullOrEmpty()) {
                //Log.d(profilePictureUri,"We should be in here")
                Image(
                    painter = rememberAsyncImagePainter(profilePictureUri),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .size(120.dp)
                )
            } else {
                // Alternative display in case user has not selected an image.
                Text("P", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = nameState,
            onValueChange = { newName -> nameState = newName },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.headlineLarge,
            label = { Text(stringResource(R.string.name_header_profile))},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.setProfileName(nameState)
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        )
    }
}
