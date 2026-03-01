package com.tructt.caro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tructt.caro.R

// ─── Font Families (matching Pencil design) ───

// Headings: Space Grotesk (variable font, supports 300–700)
val SpaceGrotesk = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_regular, FontWeight.Bold),
    Font(R.font.space_grotesk_regular, FontWeight.ExtraBold),
)

// Body: Manrope (variable font, supports 200–800)
val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_regular, FontWeight.Medium),
    Font(R.font.manrope_regular, FontWeight.SemiBold),
)

// Mono: Space Mono (timers, counters)
val SpaceMono = FontFamily(
    Font(R.font.space_mono_regular, FontWeight.Normal),
    Font(R.font.space_mono_bold, FontWeight.Bold)
)

// Legacy compat aliases
val RussoOne = SpaceGrotesk
val ChakraPetch = Manrope

// ─── Typography ───

private val GameTypography = Typography(
    // Screen titles: "VICTORY!", "DEFEAT", "DRAW!"
    displayLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        letterSpacing = 3.sp
    ),
    // Section headers: "GAME MODE", "DIFFICULTY"
    headlineLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        letterSpacing = 2.sp
    ),
    // Sub-headers: "OPPONENT'S TURN", "CANCELLED"
    headlineMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        letterSpacing = 3.sp
    ),
    // Button text: "PLAY AGAIN", "GO BACK"
    titleLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 2.sp
    ),
    // Body: descriptions, dialog text
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    // Smaller body text
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    // Button labels: "LEAVE MATCH", "MAIN MENU"
    labelLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 1.sp
    ),
    // Chip/tag labels
    labelMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )
)

// ─── Color Scheme ───

private val DarkColorScheme = darkColorScheme(
    primary = BlueAccent,
    secondary = CoralAccent,
    tertiary = AccentPurple,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = CellBorder
)

@Composable
fun CaroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = GameTypography,
        content = content
    )
}
