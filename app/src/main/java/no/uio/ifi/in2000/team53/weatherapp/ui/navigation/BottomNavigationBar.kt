package no.uio.ifi.in2000.team53.weatherapp.ui.navigation


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import no.uio.ifi.in2000.team53.weatherapp.ui.home.currentRoute

/**
 * Bottom navigation bar for the app.
 *
 * @param navController NavController for navigating between destinations.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )
    val currentRoute = currentRoute(navController)

    // Using Row for a bottom navigation approach, change to NavigationRail for a side navigation
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp, // Adjust for desired shadow
        color = MaterialTheme.colorScheme.surfaceVariant // Adjust for desired background color
    ) {
        Row {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { item.icon() },
                    label = { item.label() },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) { // Prevent reselecting the same item
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary // Highlight color for the selected item
                    )
                )
            }
        }
    }
}