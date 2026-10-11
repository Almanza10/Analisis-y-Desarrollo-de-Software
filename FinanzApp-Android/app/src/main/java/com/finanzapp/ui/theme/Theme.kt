package com.finanzapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaOscuro = darkColorScheme(
    primary = BrandA,
    onPrimary = TextMain,
    secondary = AccentPurple,
    tertiary = AccentCyan,
    background = ScreenBg,
    onBackground = TextMain,
    surface = CardBg,
    onSurface = TextMain,
    surfaceVariant = PhoneFrame,
    onSurfaceVariant = TextMuted,
    error = ErrorRed
)

/** La app solo tiene tema oscuro, igual que el diseño original. */
@Composable
fun FinanzAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaOscuro,
        typography = FinanzAppTypography,
        content = content
    )
}
