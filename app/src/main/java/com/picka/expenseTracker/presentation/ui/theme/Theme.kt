package com.picka.expenseTracker.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A6B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF6FF7F8),
    onPrimaryContainer = Color(0xFF002020),
    secondary = Color(0xFF4A6363),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E8),
    onSecondaryContainer = Color(0xFF051F1F),
    tertiary = Color(0xFF4E6079),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD5E4FF),
    onTertiaryContainer = Color(0xFF091C32),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF4FFFE),
    onBackground = Color(0xFF161D1D),
    surface = Color(0xFFF4FFFE),
    onSurface = Color(0xFF161D1D),
    surfaceVariant = Color(0xFFDAE5E5),
    onSurfaceVariant = Color(0xFF3F4949),
    outline = Color(0xFF6F7979),
    outlineVariant = Color(0xFFBEC9C9),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2B3232),
    inverseOnSurface = Color(0xFFECF2F2),
    inversePrimary = Color(0xFF4EDBDC)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4EDBDC),
    onPrimary = Color(0xFF003737),
    primaryContainer = Color(0xFF004F50),
    onPrimaryContainer = Color(0xFF6FF7F8),
    secondary = Color(0xFFB0CCCC),
    onSecondary = Color(0xFF1B3535),
    secondaryContainer = Color(0xFF324B4B),
    onSecondaryContainer = Color(0xFFCCE8E8),
    tertiary = Color(0xFFB7C8E8),
    onTertiary = Color(0xFF213248),
    tertiaryContainer = Color(0xFF374960),
    onTertiaryContainer = Color(0xFFD5E4FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0E1515),
    onBackground = Color(0xFFDEE3E3),
    surface = Color(0xFF0E1515),
    onSurface = Color(0xFFDEE3E3),
    surfaceVariant = Color(0xFF3F4949),
    onSurfaceVariant = Color(0xFFBEC9C9),
    outline = Color(0xFF899393),
    outlineVariant = Color(0xFF3F4949),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFDEE3E3),
    inverseOnSurface = Color(0xFF2B3232),
    inversePrimary = Color(0xFF006A6B)
)

@Composable
fun SmartExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
