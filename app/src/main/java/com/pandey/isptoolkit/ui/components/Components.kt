package com.pandey.isptoolkit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun StatusBadge(label: String, color: Color = SuccessGreen) {
    Surface(shape = MaterialTheme.shapes.small, color = color.copy(alpha = 0.2f)) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String = "", modifier: Modifier = Modifier, valueColor: Color = AccentCyan) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = valueColor)
            if (unit.isNotEmpty()) {
                Text(unit, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(title, style = MaterialTheme.typography.titleMedium, color = PrimaryBlue, modifier = modifier.padding(vertical = 4.dp))
}
