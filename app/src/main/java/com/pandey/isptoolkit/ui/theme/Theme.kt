package com.pandey.isptoolkit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = OnSurface,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = OnSurface,
    secondary = AccentCyan,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = OnSurface,
    surface = DarkSurface,
    onSurface = OnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = ErrorRed,
    onError = OnSurface,
)

@Composable
fun ISPToolkitTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = ISPTypography,
        content = content
    )
}
