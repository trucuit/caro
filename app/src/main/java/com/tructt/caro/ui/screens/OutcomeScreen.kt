package com.tructt.caro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.domain.GamePhase
import com.tructt.caro.domain.GameState
import com.tructt.caro.ui.components.GameBoard
import com.tructt.caro.ui.components.GradientButton
import com.tructt.caro.ui.components.SecondaryButton
import com.tructt.caro.ui.theme.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

/**
 * Unified outcome screen for Victory, Defeat, and Draw.
 * Adapts icon, title, subtitle, and accent color based on game phase.
 */
@Composable
fun OutcomeScreen(
    state: GameState,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val (icon, title, subtitle, accentColor) = when (state.phase) {
        GamePhase.WON -> OutcomeData(
            icon = Icons.Default.EmojiEvents,
            title = "VICTORY!",
            subtitle = "Congratulations",
            color = GoldAccent
        )
        GamePhase.LOST -> OutcomeData(
            icon = Icons.Default.SentimentDissatisfied,
            title = "DEFEAT",
            subtitle = "Better luck next time",
            color = CoralAccent
        )
        else -> OutcomeData(
            icon = Icons.Default.Handshake,
            title = "DRAW!",
            subtitle = "Nobody wins this round",
            color = AccentPurple
        )
    }

    val boardAlpha = if (state.phase == GamePhase.DRAW) 0.6f else 1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Icon
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = title,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            letterSpacing = 3.sp,
            color = accentColor
        )

        // Subtitle
        Text(
            text = subtitle,
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Board — faded for draw
        GameBoard(
            board = state.board,
            boardSize = state.boardConfig.size,
            enabled = false,
            onCellClick = {},
            winningCells = state.winningCells.toSet(),
            modifier = Modifier.alpha(boardAlpha)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GradientButton(
                text = "PLAY AGAIN",
                icon = Icons.Default.Refresh,
                onClick = onPlayAgain
            )
            SecondaryButton(
                text = "MAIN MENU",
                icon = Icons.Default.Home,
                onClick = onMainMenu
            )
        }
    }
}

private data class OutcomeData(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String,
    val color: Color
)
