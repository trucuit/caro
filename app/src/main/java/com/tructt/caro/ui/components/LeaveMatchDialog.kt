package com.tructt.caro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tructt.caro.ui.theme.BlueAccent
import com.tructt.caro.ui.theme.CoralAccent
import com.tructt.caro.ui.theme.DarkSurface
import com.tructt.caro.ui.theme.TextPrimary
import com.tructt.caro.ui.theme.TextSecondary

/**
 * BRD 2.1: Glassmorphism modal — frosted glass background blur
 * instead of solid dark overlay. Keeps game board contextually visible.
 */
@Composable
fun LeaveMatchDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface.copy(alpha = 0.85f),
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Leave a Match?",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = "If you leave this match it will count as lost. Are you sure you want to exit?",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    "Yes",
                    color = CoralAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    color = BlueAccent,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
