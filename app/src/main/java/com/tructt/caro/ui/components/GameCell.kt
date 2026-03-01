package com.tructt.caro.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tructt.caro.domain.CellState
import com.tructt.caro.ui.theme.BlueAccent
import com.tructt.caro.ui.theme.BlueCellBg
import com.tructt.caro.ui.theme.BgCell
import com.tructt.caro.ui.theme.BorderSubtle
import com.tructt.caro.ui.theme.CoralAccent
import com.tructt.caro.ui.theme.CoralCellBg

/**
 * BRD 2.2: Neumorphic / "Phygital" cell with subtle 3D depth.
 * BRD 2.3: Spring physics depress animation + haptic feedback.
 */
@Composable
fun GameCell(
    cellState: CellState,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    symbolPadding: Dp = 20.dp,
    isWinningCell: Boolean = false
) {
    val view = LocalView.current
    val shape = RoundedCornerShape(16.dp)

    val bgColor = when {
        cellState == CellState.X -> BlueCellBg
        cellState == CellState.O -> CoralCellBg
        else -> BgCell
    }

    val borderColor = when {
        isWinningCell && cellState == CellState.X -> BlueAccent
        isWinningCell && cellState == CellState.O -> CoralAccent
        cellState == CellState.X -> BlueAccent.copy(alpha = 0.5f)
        cellState == CellState.O -> CoralAccent.copy(alpha = 0.5f)
        else -> BorderSubtle
    }

    // BRD 2.3: Spring physics depress — scale down to 95%
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pressAnim"
    )

    // Animate symbol drawing
    var targetProgress by remember(cellState) { mutableFloatStateOf(0f) }
    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 350),
        label = "symbolAnim"
    )
    LaunchedEffect(cellState) {
        targetProgress = if (cellState != CellState.EMPTY) 1f else 0f
    }

    // BRD 2.3: Winning neon glow
    val winGlow by animateFloatAsState(
        targetValue = if (isWinningCell) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "winGlow"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            // BRD 2.2: Neumorphic shadow for "phygital" depth
            .shadow(
                elevation = if (cellState != CellState.EMPTY) 6.dp else 3.dp,
                shape = shape,
                ambientColor = when (cellState) {
                    CellState.X -> BlueAccent.copy(alpha = 0.3f)
                    CellState.O -> CoralAccent.copy(alpha = 0.3f)
                    else -> Color.Black.copy(alpha = 0.4f)
                },
                spotColor = when (cellState) {
                    CellState.X -> BlueAccent.copy(alpha = 0.2f)
                    CellState.O -> CoralAccent.copy(alpha = 0.2f)
                    else -> Color.Black.copy(alpha = 0.3f)
                }
            )
            .clip(shape)
            .background(bgColor)
            .border(
                width = if (isWinningCell) 2.dp else 1.dp,
                color = borderColor,
                shape = shape
            )
            .pointerInput(enabled, cellState) {
                if (enabled && cellState == CellState.EMPTY) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        },
                        onTap = {
                            // BRD 2.3: Haptic dopamine loop on placement
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onClick()
                        }
                    )
                }
            }
    ) {
        if (cellState != CellState.EMPTY) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(symbolPadding)
            ) {
                val strokeWidth = if (isWinningCell) 6.dp.toPx() else 4.5.dp.toPx()
                when (cellState) {
                    CellState.X -> {
                        val color = if (isWinningCell) BlueAccent else BlueAccent.copy(alpha = 0.9f)
                        val end = size.minDimension * progress
                        drawLine(
                            color = color,
                            start = Offset(0f, 0f),
                            end = Offset(end, end),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = color,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width - end, end),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    CellState.O -> {
                        val color = if (isWinningCell) CoralAccent else CoralAccent.copy(alpha = 0.9f)
                        drawArc(
                            color = color,
                            startAngle = 0f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
