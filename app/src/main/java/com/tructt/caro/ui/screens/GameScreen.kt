package com.tructt.caro.ui.screens

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tructt.caro.domain.CellState
import com.tructt.caro.domain.GameEngine
import com.tructt.caro.domain.GameMode
import com.tructt.caro.domain.GamePhase
import com.tructt.caro.presentation.GameViewModel
import com.tructt.caro.ui.components.GameBoard
import com.tructt.caro.ui.components.LeaveMatchDialog
import com.tructt.caro.ui.theme.AccentPurple
import com.tructt.caro.ui.theme.BlueAccent
import com.tructt.caro.ui.theme.ButtonBg
import com.tructt.caro.ui.theme.ButtonBorder
import com.tructt.caro.ui.theme.ChakraPetch
import com.tructt.caro.ui.theme.CoralAccent
import com.tructt.caro.ui.theme.DarkBackground
import com.tructt.caro.ui.theme.DarkSurface
import com.tructt.caro.ui.theme.RussoOne
import com.tructt.caro.ui.theme.TextPrimary
import com.tructt.caro.ui.theme.TextSecondary
import androidx.compose.foundation.layout.Box as Box

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val view = LocalView.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLocalMultiplayer = state.gameMode == GameMode.LOCAL_MULTIPLAYER

    val winningCells: Set<Int> = remember(state.board, state.phase) {
        if (state.phase == GamePhase.WON) {
            GameEngine.getWinningLine(state.board, CellState.X, state.boardConfig)?.toSet() ?: emptySet()
        } else if (state.phase == GamePhase.LOST) {
            GameEngine.getWinningLine(state.board, CellState.O, state.boardConfig)?.toSet() ?: emptySet()
        } else {
            emptySet()
        }
    }

    val isPlaying = state.phase == GamePhase.PLAYER_TURN ||
            state.phase == GamePhase.AI_THINKING ||
            state.phase == GamePhase.OPPONENT_TURN

    BackHandler(enabled = isPlaying) { viewModel.onBackPressed() }

    if (state.showLeaveDialog) {
        LeaveMatchDialog(
            onConfirm = viewModel::onConfirmLeave,
            onDismiss = viewModel::onCancelLeave
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // BRD 2.3: Icon-first status header — max 0-2 words + icon
            AnimatedContent(
                targetState = state.phase to state.currentSymbol,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "statusAnim"
            ) { (phase, _) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (phase) {
                        GamePhase.LOADING -> AnimatedGameLogo()
                        else -> {
                            // BRD 2.3: Icon mapping per state
                            val stateInfo = getStateInfo(phase, isLocalMultiplayer)

                            // Icon with accessibility contentDescription (BRD 4.7)
                            stateInfo.icon?.let { icon ->
                                val infiniteTransition = rememberInfiniteTransition(label = "iconPulse")
                                val iconScale by infiniteTransition.animateFloat(
                                    initialValue = 1f,
                                    targetValue = if (phase == GamePhase.PLAYER_TURN || phase == GamePhase.OPPONENT_TURN) 1.1f else 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(800),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "pulse"
                                )

                                Icon(
                                    imageVector = icon,
                                    contentDescription = stateInfo.accessibilityLabel,
                                    tint = stateInfo.color,
                                    modifier = Modifier
                                        .size(48.dp) // BRD 4.7: 48dp minimum touch target
                                        .scale(iconScale)
                                        .semantics {
                                            contentDescription = stateInfo.accessibilityLabel
                                        }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // BRD 2.3: Short label (0-2 words max)
                            Text(
                                text = stateInfo.shortLabel,
                                fontFamily = RussoOne,
                                fontSize = 28.sp,
                                color = stateInfo.color,
                                textAlign = TextAlign.Center,
                                letterSpacing = 2.sp
                            )

                            // Subtitle only for end states
                            stateInfo.subtitle?.let {
                                Text(
                                    text = it,
                                    fontFamily = ChakraPetch,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            GameBoard(
                board = state.board,
                boardSize = state.boardConfig.size,
                enabled = state.phase == GamePhase.PLAYER_TURN ||
                        state.phase == GamePhase.OPPONENT_TURN,
                onCellClick = viewModel::onCellClick,
                winningCells = winningCells
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom floating pill buttons
        val isEndGame = state.phase in listOf(
            GamePhase.WON, GamePhase.LOST, GamePhase.DRAW, GamePhase.CANCELLED
        )

        AnimatedVisibility(
            visible = isEndGame,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingPillButton(
                    icon = Icons.Default.Refresh,
                    text = "REMATCH",
                    contentDesc = "Play again",
                    isPrimary = true,
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        viewModel.playAgain()
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                FloatingPillButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    text = "BACK",
                    contentDesc = "Go back to menu",
                    isPrimary = false,
                    onClick = { viewModel.goToMenu() }
                )
            }
        }
    }
}

/**
 * BRD 2.3: State info mapping — icon + short label (0-2 words) + color
 */
private data class StateInfo(
    val icon: ImageVector?,
    val shortLabel: String,
    val color: androidx.compose.ui.graphics.Color,
    val accessibilityLabel: String,
    val subtitle: String? = null
)

private fun getStateInfo(phase: GamePhase, isLocal: Boolean): StateInfo = when (phase) {
    GamePhase.LOADING -> StateInfo(null, "", TextPrimary, "Loading")
    GamePhase.PLAYER_TURN -> StateInfo(
        icon = Icons.Default.TouchApp,
        shortLabel = if (isLocal) "X TURN" else "YOUR TURN",
        color = if (isLocal) BlueAccent else TextPrimary,
        accessibilityLabel = if (isLocal) "Player X's turn" else "Your turn to play"
    )
    GamePhase.OPPONENT_TURN -> StateInfo(
        icon = Icons.Default.HourglassTop,
        shortLabel = "O TURN",
        color = CoralAccent,
        accessibilityLabel = "Player O's turn"
    )
    GamePhase.AI_THINKING -> StateInfo(
        icon = Icons.Default.HourglassTop,
        shortLabel = "WAIT",
        color = TextSecondary,
        accessibilityLabel = "AI is thinking"
    )
    GamePhase.WON -> StateInfo(
        icon = Icons.Default.EmojiEvents,
        shortLabel = if (isLocal) "X WINS" else "WIN",
        color = BlueAccent,
        accessibilityLabel = if (isLocal) "Player X wins" else "You won",
        subtitle = "Congratulations"
    )
    GamePhase.LOST -> StateInfo(
        icon = Icons.Default.SentimentDissatisfied,
        shortLabel = if (isLocal) "O WINS" else "LOSE",
        color = CoralAccent,
        accessibilityLabel = if (isLocal) "Player O wins" else "You lost",
        subtitle = if (isLocal) "Great game!" else "Good luck next time"
    )
    GamePhase.DRAW -> StateInfo(
        icon = Icons.Default.Balance,
        shortLabel = "DRAW",
        color = TextPrimary,
        accessibilityLabel = "The match ended in a draw",
        subtitle = "It's a draw"
    )
    GamePhase.CANCELLED -> StateInfo(
        icon = null,
        shortLabel = "CANCELLED",
        color = TextSecondary,
        accessibilityLabel = "Match cancelled"
    )
}

@Composable
private fun AnimatedGameLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logoSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .semantics { contentDescription = "Loading game" },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(64.dp)
                .rotate(rotation)
                .scale(pulse)
        ) {
            drawArc(
                color = CoralAccent.copy(alpha = 0.7f),
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Canvas(
            modifier = Modifier
                .size(28.dp)
                .scale(pulse)
        ) {
            val sw = 3.dp.toPx()
            drawLine(BlueAccent, Offset(0f, 0f), Offset(size.width, size.height), sw, StrokeCap.Round)
            drawLine(BlueAccent, Offset(size.width, 0f), Offset(0f, size.height), sw, StrokeCap.Round)
        }
    }
}

/**
 * BRD 2.1 + 2.3: Floating pill button with icon + short label.
 */
@Composable
private fun FloatingPillButton(
    icon: ImageVector,
    text: String,
    contentDesc: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pillPress"
    )

    val pillShape = RoundedCornerShape(50)
    val bgColor = if (isPrimary) ButtonBg else DarkBackground
    val bdrColor = if (isPrimary) AccentPurple.copy(alpha = 0.5f) else ButtonBorder.copy(alpha = 0.4f)
    val txtColor = if (isPrimary) TextPrimary else TextSecondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isPrimary) 8.dp else 2.dp,
                shape = pillShape,
                ambientColor = if (isPrimary) AccentPurple.copy(alpha = 0.2f) else DarkSurface
            )
            .clip(pillShape)
            .background(bgColor)
            .border(width = 1.dp, color = bdrColor, shape = pillShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
            .padding(vertical = 14.dp)
            .semantics { contentDescription = contentDesc },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null, // parent has contentDescription
                tint = txtColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontFamily = ChakraPetch,
                fontSize = 13.sp,
                fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Medium,
                color = txtColor,
                letterSpacing = 1.5.sp
            )
        }
    }
}
