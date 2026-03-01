package com.tructt.caro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.ui.theme.*

/**
 * Game history screen — scrollable list of past matches with color-coded results.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit
) {
    // Sample data — TODO: load from local storage
    val matches = listOf(
        MatchRecord("Victory", "VS AI", "3×3", "Hard", "2m ago", BlueAccent),
        MatchRecord("Defeat", "VS AI", "3×3", "Hard", "15m ago", CoralAccent),
        MatchRecord("Victory", "Local", "3×3", "—", "1h ago", BlueAccent),
        MatchRecord("Draw", "VS AI", "5×5", "Medium", "3h ago", AccentPurple),
        MatchRecord("Victory", "VS AI", "3×3", "Easy", "5h ago", BlueAccent),
        MatchRecord("Defeat", "Local", "3×3", "—", "Yesterday", CoralAccent),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(
                    "HISTORY",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBackground,
                titleContentColor = TextPrimary
            )
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(matches) { match ->
                MatchItem(match)
            }
        }
    }
}

@Composable
private fun MatchItem(match: MatchRecord) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BgCell
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(match.color)
            )
            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = match.result,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = match.color
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${match.mode} · ${match.boardSize} · ${match.difficulty}",
                    fontFamily = Manrope,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Text(
                text = match.timeAgo,
                fontFamily = Manrope,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

private data class MatchRecord(
    val result: String,
    val mode: String,
    val boardSize: String,
    val difficulty: String,
    val timeAgo: String,
    val color: Color
)
