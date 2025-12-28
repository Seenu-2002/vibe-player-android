package com.seenu.dev.android.vibeplayer.presentation.theme

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = ButtonPrimary,
    background = Background,
    surface = Background,
    onPrimary = TextPrimary,
    onSecondary = TextSecondary,
    outline = Outline
)

val ColorScheme.buttonPrimary: Color
    get() = ButtonPrimary

val ColorScheme.buttonPrimary30: Color
    get() = ButtonPrimary30

val ColorScheme.buttonHover: Color
    get() = ButtonHover

val ColorScheme.textDisabled: Color
    get() = TextDisabled

val ColorScheme.accent: Color
    get() = Accent

val ColorScheme.surfaceHigher: Color
    get() = SurfaceHigher

@Composable
fun VibePlayerTheme(
    content: @Composable () -> Unit
) {
    SetLightStatusBarIcons()
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = VibePlayerTypography,
        content = content
    )
}

@Composable
fun SetLightStatusBarIcons() {
    val activity = (LocalActivity.current ?: return) as ComponentActivity
    val window = activity.window
    WindowInsetsControllerCompat(window, window.decorView).apply {
        isAppearanceLightStatusBars = false
    }
}