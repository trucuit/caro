package com.tructt.caro.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Loading / Splash screen — shown on app startup.
 * Logo, animated spinner, version number.
 * Auto-navigates to menu after 2 seconds.
 */
@Composable
fun LoadingScreen(
    onLoadingComplete: () -> Unit
) {
    // Auto-navigate after 2s
    LaunchedEffect(Unit) {
        delay(2000)
        onLoadingComplete()
    }

    // Spinner rotation
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // ─── Logo ───
        // Circle ring
        Box(
            modifier = Modifier
                .size(80.dp)
                .drawBehind {
                    drawCircle(
                        color = CoralAccent,
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 3.dp.toPx())
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✕",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = BlueAccent
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "TIC-TAC-TOE",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
            color = TextPrimary
        )

        // Subtitle
        Text(
            text = "CARO",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = 6.sp,
            color = AccentPurple
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ─── Spinner ───
        Box(
            modifier = Modifier
                .size(64.dp)
                .rotate(rotation)
                .drawBehind {
                    // Background ring
                    drawCircle(
                        color = BorderSubtle,
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Purple arc (120°)
                    drawArc(
                        color = AccentPurple,
                        startAngle = 0f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset.Zero,
                        size = Size(size.width, size.height),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Loading",
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.weight(1f))

        // ─── Version ───
        Text(
            text = "v1.0.0",
            fontFamily = SpaceMono,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}
