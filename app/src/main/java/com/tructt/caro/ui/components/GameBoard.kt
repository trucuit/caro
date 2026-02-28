package com.tructt.caro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tructt.caro.domain.CellState

@Composable
fun GameBoard(
    board: List<CellState>,
    boardSize: Int,
    enabled: Boolean,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    winningCells: Set<Int> = emptySet()
) {
    val spacing = if (boardSize <= 3) 12.dp else if (boardSize <= 5) 6.dp else 4.dp
    val cellPadding = if (boardSize <= 3) 20.dp else if (boardSize <= 5) 12.dp else 8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (boardSize <= 3) 24.dp else 12.dp),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (row in 0 until boardSize) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                for (col in 0 until boardSize) {
                    val index = row * boardSize + col
                    GameCell(
                        cellState = board[index],
                        enabled = enabled,
                        symbolPadding = cellPadding,
                        isWinningCell = index in winningCells,
                        onClick = { onCellClick(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
