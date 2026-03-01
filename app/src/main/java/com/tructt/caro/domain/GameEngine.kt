package com.tructt.caro.domain

/**
 * Pure game logic engine — no side effects, easy to unit-test.
 * Supports variable board sizes and win lengths (BRD 6.2).
 */
object GameEngine {

    /**
     * Generate all possible winning lines for a given board config.
     */
    fun getWinCombinations(config: BoardConfig): List<List<Int>> {
        val combinations = mutableListOf<List<Int>>()
        val size = config.size
        val winLen = config.winLength

        // Rows
        for (row in 0 until size) {
            for (startCol in 0..size - winLen) {
                combinations.add((0 until winLen).map { row * size + startCol + it })
            }
        }
        // Columns
        for (col in 0 until size) {
            for (startRow in 0..size - winLen) {
                combinations.add((0 until winLen).map { (startRow + it) * size + col })
            }
        }
        // Diagonals (top-left to bottom-right)
        for (row in 0..size - winLen) {
            for (col in 0..size - winLen) {
                combinations.add((0 until winLen).map { (row + it) * size + (col + it) })
            }
        }
        // Diagonals (top-right to bottom-left)
        for (row in 0..size - winLen) {
            for (col in winLen - 1 until size) {
                combinations.add((0 until winLen).map { (row + it) * size + (col - it) })
            }
        }
        return combinations
    }

    /**
     * Place the player's symbol at [index] and evaluate the board.
     * Returns null if the move is invalid.
     */
    fun makePlayerMove(state: GameState, index: Int): GameState? {
        val totalCells = state.boardConfig.totalCells
        if (index !in 0 until totalCells || state.board[index] != CellState.EMPTY) return null
        if (state.phase != GamePhase.PLAYER_TURN) return null

        val newBoard = state.board.toMutableList().apply { this[index] = CellState.X }
        return evaluateBoard(state, newBoard, justPlaced = CellState.X)
    }

    /**
     * Place a move for local multiplayer.
     */
    fun makeLocalMove(state: GameState, index: Int): GameState? {
        val totalCells = state.boardConfig.totalCells
        if (index !in 0 until totalCells || state.board[index] != CellState.EMPTY) return null
        if (state.phase != GamePhase.PLAYER_TURN && state.phase != GamePhase.OPPONENT_TURN) return null

        val symbol = state.currentSymbol
        val newBoard = state.board.toMutableList().apply { this[index] = symbol }
        return evaluateLocalBoard(state, newBoard, justPlaced = symbol)
    }

    /**
     * AI picks a move and places O.
     */
    fun makeAiMove(state: GameState): GameState {
        val board = state.board.toMutableList()
        val bestIndex = when (state.aiDifficulty) {
            AiDifficulty.EASY -> findRandomMove(board)
            AiDifficulty.MEDIUM -> findMediumMove(board, state.boardConfig)
            AiDifficulty.HARD -> {
                if (state.boardConfig.size <= 3) {
                    findBestMove(board, state.boardConfig)
                } else {
                    // Minimax is too slow for larger boards, use heuristic
                    findMediumMove(board, state.boardConfig)
                }
            }
        }
        if (bestIndex != -1) {
            board[bestIndex] = CellState.O
        }
        return evaluateBoard(state, board, justPlaced = CellState.O)
    }

    /**
     * Get the indices of the winning line, if any, for [player].
     */
    fun getWinningLine(board: List<CellState>, player: CellState, config: BoardConfig): List<Int>? {
        return getWinCombinations(config).firstOrNull { combo ->
            combo.all { board[it] == player }
        }
    }

    // ---------- Internal helpers ----------

    private fun evaluateBoard(state: GameState, board: List<CellState>, justPlaced: CellState): GameState {
        val config = state.boardConfig
        val winLine = getWinningLine(board, justPlaced, config)
        if (winLine != null) {
            val phase = if (justPlaced == CellState.X) GamePhase.WON else GamePhase.LOST
            return state.copy(board = board, phase = phase, winningCells = winLine)
        }
        if (board.none { it == CellState.EMPTY }) {
            return state.copy(board = board, phase = GamePhase.DRAW)
        }
        val nextPhase = if (justPlaced == CellState.X) GamePhase.AI_THINKING else GamePhase.PLAYER_TURN
        return state.copy(board = board, phase = nextPhase)
    }

    private fun evaluateLocalBoard(state: GameState, board: List<CellState>, justPlaced: CellState): GameState {
        val config = state.boardConfig
        val winLine = getWinningLine(board, justPlaced, config)
        if (winLine != null) {
            // In local multiplayer, X winning = WON, O winning = LOST (from X's perspective)
            val phase = if (justPlaced == CellState.X) GamePhase.WON else GamePhase.LOST
            return state.copy(board = board, phase = phase, winningCells = winLine)
        }
        if (board.none { it == CellState.EMPTY }) {
            return state.copy(board = board, phase = GamePhase.DRAW)
        }
        // Alternate turns
        val nextSymbol = if (justPlaced == CellState.X) CellState.O else CellState.X
        val nextPhase = if (nextSymbol == CellState.X) GamePhase.PLAYER_TURN else GamePhase.OPPONENT_TURN
        return state.copy(board = board, phase = nextPhase, currentSymbol = nextSymbol)
    }

    private fun hasWon(board: List<CellState>, player: CellState, config: BoardConfig): Boolean {
        return getWinCombinations(config).any { combo -> combo.all { board[it] == player } }
    }

    // ---- AI strategies ----

    private fun findRandomMove(board: List<CellState>): Int {
        val empties = board.indices.filter { board[it] == CellState.EMPTY }
        return if (empties.isNotEmpty()) empties.random() else -1
    }

    private fun findMediumMove(board: MutableList<CellState>, config: BoardConfig): Int {
        val combos = getWinCombinations(config)
        // 1. Try to win
        for (combo in combos) {
            val oCount = combo.count { board[it] == CellState.O }
            val emptyCount = combo.count { board[it] == CellState.EMPTY }
            if (oCount == config.winLength - 1 && emptyCount == 1) {
                return combo.first { board[it] == CellState.EMPTY }
            }
        }
        // 2. Block player
        for (combo in combos) {
            val xCount = combo.count { board[it] == CellState.X }
            val emptyCount = combo.count { board[it] == CellState.EMPTY }
            if (xCount == config.winLength - 1 && emptyCount == 1) {
                return combo.first { board[it] == CellState.EMPTY }
            }
        }
        // 3. Take center if available
        val center = config.size / 2 * config.size + config.size / 2
        if (board[center] == CellState.EMPTY) return center
        // 4. Random
        return findRandomMove(board)
    }

    // ---- Minimax AI (for 3x3 only) ----

    private fun findBestMove(board: MutableList<CellState>, config: BoardConfig): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1
        for (i in board.indices) {
            if (board[i] == CellState.EMPTY) {
                board[i] = CellState.O
                val score = minimax(board, config, depth = 0, isMaximizing = false)
                board[i] = CellState.EMPTY
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: MutableList<CellState>, config: BoardConfig, depth: Int, isMaximizing: Boolean): Int {
        if (hasWon(board, CellState.O, config)) return 10 - depth
        if (hasWon(board, CellState.X, config)) return depth - 10
        if (board.none { it == CellState.EMPTY }) return 0

        if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i] == CellState.EMPTY) {
                    board[i] = CellState.O
                    best = maxOf(best, minimax(board, config, depth + 1, false))
                    board[i] = CellState.EMPTY
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i] == CellState.EMPTY) {
                    board[i] = CellState.X
                    best = minOf(best, minimax(board, config, depth + 1, true))
                    board[i] = CellState.EMPTY
                }
            }
            return best
        }
    }
}
