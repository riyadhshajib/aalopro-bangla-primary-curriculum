package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PremiumPurple,
    onPrimary = Color.White,
    primaryContainer = PremiumPurpleContainer,
    onPrimaryContainer = Color(0xFF28594F),
    secondary = PremiumTeal,
    onSecondary = Color.White,
    secondaryContainer = PremiumTealContainer,
    onSecondaryContainer = Color(0xFF28594F),
    background = WarmPaperBg,
    onBackground = WarmTextPrimary,
    surface = WarmPaperSurface,
    onSurface = WarmTextPrimary,
    surfaceVariant = WarmPaperCard,
    onSurfaceVariant = WarmTextSecondary,
    outline = WarmPaperBorder,
    outlineVariant = Color(0xFFE8DCC7)
)

private val DarkColorScheme = darkColorScheme(
    primary = AaloYellow,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2A2312),
    onPrimaryContainer = AaloYellowLight,
    secondary = AaloYellowLight,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF383015),
    onSecondaryContainer = Color(0xFFFFF3C4),
    background = DarkBackground,
    onBackground = Color(0xFFF5F5F5),
    surface = DarkSurface,
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = DarkBorder,
    outlineVariant = Color(0xFF2E2B22)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to Aalo Warm Child-Friendly Theme for Primary Kids
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
