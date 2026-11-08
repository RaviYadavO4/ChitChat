package com.portfolio.chitchat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Premium Light Color Palette
private val LightColorScheme = lightColorScheme(
    primary = WhatsappTealGreen,
    secondary = WhatsappDarkGreen,
    tertiary = WhatsappLightGreen,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF7F7F7),
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A)
)

// Premium Modern Dark Color Palette (WhatsApp Style)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1F2C34),
    secondary = Color(0xFF111B21),
    tertiary = WhatsappLightGreen,
    background = Color(0xFF0B141A),
    surface = Color(0xFF111B21),
    onBackground = Color(0xFFE9EDEF),
    onSurface = Color(0xFFE9EDEF)
)

@Composable
fun ChitChatTheme(
    isDarkMode: Boolean = false, // Dynamic runtime state trigger
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
