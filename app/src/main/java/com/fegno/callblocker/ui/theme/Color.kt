package com.fegno.callblocker.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---- Brand / accent constants ----
val lime_neon = Color(0xFFB6FF3C) // dark-surface accent fill
val lime_neon_deep = Color(0xFF8BC926) // light-theme accent variant
val on_lime = Color(0xFF1A2400) // near-black text on lime cards
val delta_positive_dark = Color(0xFF8BE04A)
val delta_positive_light = Color(0xFF3E7A00)
val delta_negative = Color(0xFFFF5A5A)
val delta_negative_light = Color(0xFFBA1A1A)

// ---- Light scheme colors ----
val md_light_primary = Color(0xFF4E6A00)
val md_light_onPrimary = Color(0xFFFFFFFF)
val md_light_primaryContainer = Color(0xFFC4F24E)
val md_light_onPrimaryContainer = Color(0xFF1A2400)
val md_light_secondary = Color(0xFF5A6147)
val md_light_onSecondary = Color(0xFFFFFFFF)
val md_light_secondaryContainer = Color(0xFFE3E8CF)
val md_light_onSecondaryContainer = Color(0xFF181D08)
val md_light_tertiary = Color(0xFF3E6A00)
val md_light_onTertiary = Color(0xFFFFFFFF)
val md_light_tertiaryContainer = Color(0xFFB7F25A)
val md_light_onTertiaryContainer = Color(0xFF152200)
val md_light_error = Color(0xFFBA1A1A)
val md_light_onError = Color(0xFFFFFFFF)
val md_light_errorContainer = Color(0xFFFFDAD6)
val md_light_onErrorContainer = Color(0xFF410002)
val md_light_background = Color(0xFFF7F9F0)
val md_light_onBackground = Color(0xFF1A1C16)
val md_light_surface = Color(0xFFF7F9F0)
val md_light_onSurface = Color(0xFF1A1C16)
val md_light_surfaceVariant = Color(0xFFE3E8D4)
val md_light_onSurfaceVariant = Color(0xFF45483D)
val md_light_outline = Color(0xFF767869)
val md_light_surfaceContainerLowest = Color(0xFFFFFFFF)
val md_light_surfaceContainerLow = Color(0xFFF1F4E8)
val md_light_surfaceContainer = Color(0xFFEBEEDF)
val md_light_surfaceContainerHigh = Color(0xFFE5E9DA)
val md_light_surfaceContainerHighest = Color(0xFFDFE3D4)

// ---- Dark scheme colors ----
val md_dark_primary = Color(0xFFB6FF3C)
val md_dark_onPrimary = Color(0xFF1A2400)
val md_dark_primaryContainer = Color(0xFF2A3A00)
val md_dark_onPrimaryContainer = Color(0xFFCFFF7A)
val md_dark_secondary = Color(0xFFC9CDB8)
val md_dark_onSecondary = Color(0xFF1C1C18)
val md_dark_secondaryContainer = Color(0xFF26271F)
val md_dark_onSecondaryContainer = Color(0xFFE7EBD6)
val md_dark_tertiary = Color(0xFF9EE53D)
val md_dark_onTertiary = Color(0xFF1A2400)
val md_dark_tertiaryContainer = Color(0xFF243200)
val md_dark_onTertiaryContainer = Color(0xFFD3FF8A)
val md_dark_error = Color(0xFFFF5A5A)
val md_dark_onError = Color(0xFF2A0000)
val md_dark_errorContainer = Color(0xFF3A1212)
val md_dark_onErrorContainer = Color(0xFFFFD9D6)
val md_dark_background = Color(0xFF080809)
val md_dark_onBackground = Color(0xFFF3F3F4)
val md_dark_surface = Color(0xFF080809)
val md_dark_onSurface = Color(0xFFF3F3F4)
val md_dark_surfaceVariant = Color(0xFF1B1B1D)
val md_dark_onSurfaceVariant = Color(0xFF9B9BA0)
val md_dark_outline = Color(0xFF2E2E31)
val md_dark_surfaceContainerLowest = Color(0xFF050506)
val md_dark_surfaceContainerLow = Color(0xFF131315)
val md_dark_surfaceContainer = Color(0xFF161618)
val md_dark_surfaceContainerHigh = Color(0xFF202023)
val md_dark_surfaceContainerHighest = Color(0xFF29292D)

// ---- Custom semantic colors (no M3 ColorScheme slot) ----
@Immutable
data class CallBlockerColors(
    val accentFill: Color,
    val onAccent: Color,
    val deltaPositive: Color,
    val deltaNegative: Color,
    val track: Color,
    val trackFilled: Color,
)

val DarkAccents = CallBlockerColors(
    accentFill = lime_neon,
    onAccent = on_lime,
    deltaPositive = delta_positive_dark,
    deltaNegative = delta_negative,
    track = Color(0xFF2C2C30),
    trackFilled = lime_neon,
)

val LightAccents = CallBlockerColors(
    accentFill = md_light_primaryContainer,
    onAccent = md_light_onPrimaryContainer,
    deltaPositive = delta_positive_light,
    deltaNegative = delta_negative_light,
    track = Color(0xFFD3DABF),
    trackFilled = lime_neon_deep,
)

val LocalCallBlockerColors = staticCompositionLocalOf { DarkAccents }
