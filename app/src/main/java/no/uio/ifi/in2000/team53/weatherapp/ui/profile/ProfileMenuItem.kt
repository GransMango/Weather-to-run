package no.uio.ifi.in2000.team53.weatherapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable component for a profile menu item.
 * @param text The text to display in the menu item.
 * @param onClick The action to perform when the menu item is clicked.
 */
@Composable
fun ProfileMenuItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Text(text = text)
    }
}