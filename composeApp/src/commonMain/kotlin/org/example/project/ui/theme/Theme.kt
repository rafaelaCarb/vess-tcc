package org.example.project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryModernBlueGray,
    onPrimary = Color.White,
    primaryContainer = PrimaryModernSoftTeal,
    onPrimaryContainer = TextModernPrimary,

    secondary = SecondaryModernWarmGray,
    onSecondary = TextModernPrimary,
    secondaryContainer = SecondaryModernOffWhite,
    onSecondaryContainer = TextModernSecondary,

    tertiary = SecondaryModernLightGray,
    onTertiary = TextModernPrimary,
    tertiaryContainer = SecondaryModernLightGray,
    onTertiaryContainer = TextModernSecondary,

    background = BackgroundModernLight,
    onBackground = TextModernPrimary,

    surface = BackgroundModernCard,
    onSurface = TextModernPrimary,

    surfaceVariant = SurfaceModernVariant,
    onSurfaceVariant = TextModernSecondary,

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = TextModernHint,
    scrim = Color(0x99000000)
)

@Composable
fun AppVessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}