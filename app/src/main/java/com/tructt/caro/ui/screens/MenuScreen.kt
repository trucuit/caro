package com.tructt.caro.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.domain.AiDifficulty
import com.tructt.caro.domain.GameMode
import com.tructt.caro.ui.components.GradientButton
import com.tructt.caro.ui.theme.*

@Composable
fun MenuScreen(
    onStartGame: (GameMode, AiDifficulty, Int) -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf(GameMode.VS_AI) }
    var selectedDifficulty by remember { mutableStateOf(AiDifficulty.HARD) }
    var selectedBoardSize by remember { mutableIntStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top navigation icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onNavigateToProfile) {
                Icon(Icons.Default.Person, "Profile", tint = TextSecondary)
            }
            IconButton(onClick = onNavigateToHistory) {
                Icon(Icons.Default.History, "History", tint = TextSecondary)
            }
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Default.Settings, "Settings", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logo: O circle + X inside
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(60.dp)) {
                drawCircle(
                    color = CoralAccent,
                    radius = size.minDimension / 2,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
            Canvas(modifier = Modifier.size(28.dp)) {
                val pad = 2.dp.toPx()
                drawLine(
                    color = BlueAccent,
                    start = Offset(pad, pad),
                    end = Offset(size.width - pad, size.height - pad),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BlueAccent,
                    start = Offset(size.width - pad, pad),
                    end = Offset(pad, size.height - pad),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "TIC-TAC-TOE",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
            color = TextPrimary
        )
        Text(
            text = "CARO",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = 6.sp,
            color = AccentPurple
        )

        Spacer(modifier = Modifier.height(40.dp))

        // GAME MODE
        SectionLabel("GAME MODE")
        Row(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectableChip(
                text = "VS AI",
                selected = selectedMode == GameMode.VS_AI,
                onClick = { selectedMode = GameMode.VS_AI },
                modifier = Modifier.weight(1f)
            )
            SelectableChip(
                text = "LOCAL",
                selected = selectedMode == GameMode.LOCAL_MULTIPLAYER,
                onClick = { selectedMode = GameMode.LOCAL_MULTIPLAYER },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // DIFFICULTY (only for VS AI)
        if (selectedMode == GameMode.VS_AI) {
            SectionLabel("DIFFICULTY")
            Row(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AiDifficulty.entries.forEach { diff ->
                    SelectableChip(
                        text = diff.name,
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // BOARD SIZE
        SectionLabel("BOARD SIZE")
        Row(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(3, 5, 7).forEach { size ->
                SelectableChip(
                    text = "${size}×${size}",
                    selected = selectedBoardSize == size,
                    onClick = { selectedBoardSize = size },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // PLAY button
        Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp)) {
            GradientButton(
                text = "PLAY",
                onClick = {
                    onStartGame(selectedMode, selectedDifficulty, selectedBoardSize)
                }
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 2.sp,
        color = TextMuted,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp)
    )
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        if (selected) AccentPurple.copy(alpha = 0.15f) else BgCell,
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        if (selected) AccentPurple else BorderSubtle,
        label = "chipBorder"
    )
    val textColor by animateColorAsState(
        if (selected) AccentPurple else TextSecondary,
        label = "chipText"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
