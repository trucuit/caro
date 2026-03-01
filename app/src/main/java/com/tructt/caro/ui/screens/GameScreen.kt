package com.tructt.caro.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.domain.GamePhase
import com.tructt.caro.domain.GameState
import com.tructt.caro.presentation.GameViewModel
import com.tructt.caro.ui.components.GameBoard
import com.tructt.caro.ui.components.LeaveMatchDialog
import com.tructt.caro.ui.components.SecondaryButton
import com.tructt.caro.ui.theme.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameEnd: (GamePhase) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Navigate to outcome screen when game ends
    LaunchedEffect(state.phase) {
        when (state.phase) {
            GamePhase.WON, GamePhase.LOST, GamePhase.DRAW, GamePhase.CANCELLED -> {
                kotlinx.coroutines.delay(600) // Brief delay for last move animation
                onGameEnd(state.phase)
            }
            else -> {}
        }
    }

    BackHandler {
        viewModel.onBackPressed()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        when (state.phase) {
            GamePhase.LOADING -> LoadingContent()
            else -> GameContent(state, viewModel, onGameEnd)
        }

        // Leave match dialog
        if (state.showLeaveDialog) {
            LeaveMatchDialog(
                onConfirm = { viewModel.onConfirmLeave() },
                onDismiss = { viewModel.onCancelLeave() }
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = AccentPurple,
                trackColor = BorderSubtle,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Loading",
                fontFamily = Manrope,
                fontSize = 14.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun GameContent(
    state: GameState,
    viewModel: GameViewModel,
    onGameEnd: (GamePhase) -> Unit
) {
    val isPlayerTurn = state.phase == GamePhase.PLAYER_TURN
    val isOpponentTurn = state.phase == GamePhase.OPPONENT_TURN || state.phase == GamePhase.AI_THINKING
    val boardEnabled = isPlayerTurn || (state.phase == GamePhase.OPPONENT_TURN &&
            state.gameMode == com.tructt.caro.domain.GameMode.LOCAL_MULTIPLAYER)
    val boardAlpha = if (isOpponentTurn && state.gameMode == com.tructt.caro.domain.GameMode.VS_AI) 0.7f else 1f

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // ─── Turn timer ───
        var turnSeconds by remember { mutableIntStateOf(0) }
        LaunchedEffect(state.phase) {
            turnSeconds = 0
            while (true) {
                kotlinx.coroutines.delay(1000)
                turnSeconds++
            }
        }
        val timerText = "%d:%02d".format(turnSeconds / 60, turnSeconds % 60)

        // HUD: turn indicator
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = isPlayerTurn,
                label = "turnHud"
            ) { playerTurn ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (playerTurn) Icons.Default.Fingerprint else Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = if (playerTurn) TextPrimary else TextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (playerTurn) "YOUR TURN" else "OPPONENT'S TURN",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        letterSpacing = 3.sp,
                        color = if (playerTurn) TextPrimary else TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = timerText,
                        fontFamily = SpaceMono,
                        fontSize = 16.sp,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Board
        GameBoard(
            board = state.board,
            boardSize = state.boardConfig.size,
            enabled = boardEnabled,
            onCellClick = { viewModel.onCellClick(it) },
            winningCells = state.winningCells.toSet(),
            modifier = Modifier.alpha(boardAlpha)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Waiting indicator for AI turn
        if (isOpponentTurn && state.gameMode == com.tructt.caro.domain.GameMode.VS_AI) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = AccentPurple,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Waiting for opponent...",
                    fontFamily = Manrope,
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }
        }

        // Leave match button
        Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp)) {
            SecondaryButton(
                text = "LEAVE MATCH",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { viewModel.onBackPressed() }
            )
        }
    }
}
