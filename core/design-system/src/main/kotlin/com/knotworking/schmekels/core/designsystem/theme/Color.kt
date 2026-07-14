package com.knotworking.schmekels.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1B5E20)
val PrimaryDark = Color(0xFF4CAF50)
val Secondary = Color(0xFF00695C)
val SecondaryDark = Color(0xFF4DB6AC)
val Background = Color(0xFFFDFDF6)
val BackgroundDark = Color(0xFF121212)
val Surface = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)
val Error = Color(0xFFB00020)
val ErrorDark = Color(0xFFCF6679)

val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    error = Error
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark
)
