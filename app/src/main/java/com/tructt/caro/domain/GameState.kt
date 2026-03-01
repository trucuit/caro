package com.tructt.caro.domain

/**
 * Represents who occupies a cell on the board.
 */
enum class CellState {
    EMPTY, X, O
}

/**
 * The current phase of the game.
 */
enum class GamePhase {
    LOADING,
    PLAYER_TURN,
    AI_THINKING,
    OPPONENT_TURN,   // For local multiplayer (pass & play)
    WON,
    LOST,
    DRAW,
    CANCELLED
}

/**
 * Game mode selection.
 */
enum class GameMode {
    VS_AI,
    LOCAL_MULTIPLAYER
}

/**
 * AI Difficulty (BRD 6.1).
 */
enum class AiDifficulty {
    EASY,    // Random moves
    MEDIUM,  // 50% smart, 50% random
    HARD     // Full minimax
}

/**
 * Board configuration (BRD 6.2).
 */
data class BoardConfig(
    val size: Int = 3,
    val winLength: Int = 3    // 3-in-a-row for 3x3, 5-in-a-row for larger
) {
    val totalCells: Int get() = size * size
}

/**
 * Immutable snapshot of the entire game state.
 */
data class GameState(
    val board: List<CellState> = List(9) { CellState.EMPTY },
    val phase: GamePhase = GamePhase.LOADING,
    val showLeaveDialog: Boolean = false,
    val gameMode: GameMode = GameMode.VS_AI,
    val aiDifficulty: AiDifficulty = AiDifficulty.HARD,
    val boardConfig: BoardConfig = BoardConfig(),
    val currentSymbol: CellState = CellState.X,  // Tracks whose turn in local multiplayer
    val winningCells: List<Int> = emptyList()     // Indices of winning line
)
