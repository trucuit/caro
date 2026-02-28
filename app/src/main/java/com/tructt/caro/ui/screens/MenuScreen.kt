package com.tructt.caro.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.R
import com.tructt.caro.domain.AiDifficulty
import com.tructt.caro.domain.GameMode
import com.tructt.caro.ui.theme.AccentPurple
import com.tructt.caro.ui.theme.BlueAccent
import com.tructt.caro.ui.theme.CellBorder
import com.tructt.caro.ui.theme.ChakraPetch
import com.tructt.caro.ui.theme.CoralAccent
import com.tructt.caro.ui.theme.DarkBackground
import com.tructt.caro.ui.theme.DarkSurface
import com.tructt.caro.ui.theme.RussoOne
import com.tructt.caro.ui.theme.TextPrimary
import com.tructt.caro.ui.theme.TextSecondary

@Composable
fun MenuScreen(
    onStartGame: (GameMode, AiDifficulty, Int) -> Unit
) {
    var selectedMode by remember { mutableStateOf(GameMode.VS_AI) }
    var selectedDifficulty by remember { mutableStateOf(AiDifficulty.HARD) }
    var selectedBoardSize by remember { mutableIntStateOf(3) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val privacyPolicyUrl = stringResource(R.string.privacy_policy_url)

    if (showPrivacyPolicy) {
        PrivacyPolicyDialog(
            onDismiss = { showPrivacyPolicy = false },
            onOpenUrl = { uriHandler.openUri(privacyPolicyUrl) },
            hasExternalUrl = privacyPolicyUrl.isNotBlank()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(60.dp)) {
                    drawArc(
                        color = CoralAccent,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Canvas(modifier = Modifier.size(30.dp)) {
                    val sw = 4.dp.toPx()
                    drawLine(BlueAccent, Offset(0f, 0f), Offset(size.width, size.height), sw, StrokeCap.Round)
                    drawLine(BlueAccent, Offset(size.width, 0f), Offset(0f, size.height), sw, StrokeCap.Round)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title — Russo One gaming font
            Text(
                text = "TIC-TAC-TOE",
                fontFamily = RussoOne,
                fontSize = 32.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp
            )
            Text(
                text = "CARO",
                fontFamily = ChakraPetch,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AccentPurple,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Game Mode
            SectionLabel("GAME MODE")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SelectableChip(
                    text = "VS AI",
                    selected = selectedMode == GameMode.VS_AI,
                    onClick = { selectedMode = GameMode.VS_AI },
                    modifier = Modifier.weight(1f)
                )
                SelectableChip(
                    text = "2 PLAYERS",
                    selected = selectedMode == GameMode.LOCAL_MULTIPLAYER,
                    onClick = { selectedMode = GameMode.LOCAL_MULTIPLAYER },
                    modifier = Modifier.weight(1f)
                )
            }

            if (selectedMode == GameMode.VS_AI) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionLabel("DIFFICULTY")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SelectableChip(
                        text = "EASY",
                        selected = selectedDifficulty == AiDifficulty.EASY,
                        onClick = { selectedDifficulty = AiDifficulty.EASY },
                        modifier = Modifier.weight(1f)
                    )
                    SelectableChip(
                        text = "MEDIUM",
                        selected = selectedDifficulty == AiDifficulty.MEDIUM,
                        onClick = { selectedDifficulty = AiDifficulty.MEDIUM },
                        modifier = Modifier.weight(1f)
                    )
                    SelectableChip(
                        text = "HARD",
                        selected = selectedDifficulty == AiDifficulty.HARD,
                        onClick = { selectedDifficulty = AiDifficulty.HARD },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Board Size
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel("BOARD SIZE")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SelectableChip(
                    text = "3×3",
                    selected = selectedBoardSize == 3,
                    onClick = { selectedBoardSize = 3 },
                    modifier = Modifier.weight(1f)
                )
                SelectableChip(
                    text = "5×5",
                    selected = selectedBoardSize == 5,
                    onClick = { selectedBoardSize = 5 },
                    modifier = Modifier.weight(1f)
                )
                SelectableChip(
                    text = "7×7",
                    selected = selectedBoardSize == 7,
                    onClick = { selectedBoardSize = 7 },
                    modifier = Modifier.weight(1f)
                )
            }
            if (selectedBoardSize > 3) {
                Text(
                    text = "Win condition: 5-in-a-row",
                    fontFamily = ChakraPetch,
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Play button
            PlayButton(
                onClick = {
                    onStartGame(selectedMode, selectedDifficulty, selectedBoardSize)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { showPrivacyPolicy = true }) {
                Text(
                    text = stringResource(R.string.privacy_policy_title),
                    fontFamily = ChakraPetch,
                    fontSize = 12.sp,
                    color = BlueAccent
                )
            }
        }
    }
}

@Composable
private fun PrivacyPolicyDialog(
    onDismiss: () -> Unit,
    onOpenUrl: () -> Unit,
    hasExternalUrl: Boolean
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        shape = RoundedCornerShape(18.dp),
        title = {
            Text(
                text = stringResource(R.string.privacy_policy_title),
                color = TextPrimary,
                fontFamily = RussoOne,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 280.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy_summary),
                    color = TextSecondary,
                    fontFamily = ChakraPetch,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    color = BlueAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            if (hasExternalUrl) {
                TextButton(onClick = onOpenUrl) {
                    Text(
                        text = stringResource(R.string.open_full_policy),
                        color = AccentPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = ChakraPetch,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary.copy(alpha = 0.5f),
        letterSpacing = 2.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
    )
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    val bgColor by animateColorAsState(
        targetValue = if (selected) AccentPurple.copy(alpha = 0.12f) else DarkSurface,
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) AccentPurple.copy(alpha = 0.5f) else CellBorder.copy(alpha = 0.3f),
        label = "chipBorder"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) AccentPurple else TextSecondary,
        label = "chipText"
    )

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chipPress"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(elevation = if (selected) 4.dp else 0.dp, shape = shape, ambientColor = AccentPurple.copy(alpha = 0.2f))
            .clip(shape)
            .background(bgColor)
            .border(width = 1.dp, color = borderColor, shape = shape)
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
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = ChakraPetch,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun PlayButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "playPress"
    )

    val pillShape = RoundedCornerShape(50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(elevation = 12.dp, shape = pillShape, ambientColor = AccentPurple.copy(alpha = 0.4f))
            .clip(pillShape)
            .background(AccentPurple)
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
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "PLAY",
            fontFamily = RussoOne,
            fontSize = 18.sp,
            color = TextPrimary,
            letterSpacing = 3.sp
        )
    }
}
