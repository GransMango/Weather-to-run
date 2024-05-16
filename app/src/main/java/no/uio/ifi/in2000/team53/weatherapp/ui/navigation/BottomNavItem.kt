package no.uio.ifi.in2000.team53.weatherapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Sealed class for bottom navigation items.
 *
 * @param route String representing the route of the item.
 * @param label Composable representing the label of the item.
 * @param icon Composable representing the icon of the item.
 */
sealed class BottomNavItem(val route: String, val label: @Composable () -> Unit, val icon: @Composable () -> Unit) {
    data object Home : BottomNavItem("home",
        { Text(text = stringResource(id = R.string.home)) },
        { Icon(Icons.Filled.Home, contentDescription = stringResource(id = R.string.home_icon_desc)) }
    )
    data object Profile : BottomNavItem("profile",
        { Text(text = stringResource(id = R.string.profile)) },
        { Icon(Icons.Filled.Person, contentDescription = stringResource(id = R.string.person_icon_desc)) }
    )
    data object Settings : BottomNavItem("settings",
        { Text(text = stringResource(id = R.string.settings)) },
        { Icon(Icons.Filled.Settings, contentDescription = stringResource(id = R.string.settings_icon_desc)) }
    )
}