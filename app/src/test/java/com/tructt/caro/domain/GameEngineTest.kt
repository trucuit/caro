package com.tructt.caro.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    // ---- 3x3 Board Tests ----

    @Test
    fun `initial move on empty board succeeds`() {
        val state = GameState(phase = GamePhase.PLAYER_TURN)
        val result = GameEngine.makePlayerMove(state, 0)
        assertNotNull(result)
        assertEquals(CellState.X, result!!.board[0])
    }

    @Test
    fun `move on occupied cell returns null`() {
        val board = MutableList(9) { CellState.EMPTY }
        board[0] = CellState.X
        val state = GameState(board = board, phase = GamePhase.PLAYER_TURN)
        val result = GameEngine.makePlayerMove(state, 0)
        assertNull(result)
    }

    @Test
    fun `player wins with left column`() {
        val board = MutableList(9) { CellState.EMPTY }
        board[0] = CellState.X
        board[3] = CellState.X
        val state = GameState(board = board, phase = GamePhase.PLAYER_TURN)
        val result = GameEngine.makePlayerMove(state, 6)
        assertNotNull(result)
        assertEquals(GamePhase.WON, result!!.phase)
    }

    @Test
    fun `draw detection when board is full`() {
        // X O X
        // X X O
        // O X ?  -> last cell = O results in draw
        val board = mutableListOf(
            CellState.X, CellState.O, CellState.X,
            CellState.X, CellState.X, CellState.O,
            CellState.O, CellState.X, CellState.EMPTY
        )
        val state = GameState(board = board, phase = GamePhase.AI_THINKING)
        val result = GameEngine.makeAiMove(state)
        assertEquals(GamePhase.DRAW, result.phase)
    }

    @Test
    fun `AI picks a valid move`() {
        val state = GameState(phase = GamePhase.AI_THINKING)
        val result = GameEngine.makeAiMove(state)
        val oCount = result.board.count { it == CellState.O }
        assertEquals(1, oCount)
    }

    @Test
    fun `move during wrong phase returns null`() {
        val state = GameState(phase = GamePhase.LOADING)
        val result = GameEngine.makePlayerMove(state, 4)
        assertNull(result)
    }

    // ---- AI Difficulty Tests ----

    @Test
    fun `easy AI makes a valid move`() {
        val state = GameState(
            phase = GamePhase.AI_THINKING,
            aiDifficulty = AiDifficulty.EASY
        )
        val result = GameEngine.makeAiMove(state)
        val oCount = result.board.count { it == CellState.O }
        assertEquals(1, oCount)
    }

    @Test
    fun `medium AI blocks player about to win`() {
        // X X _
        // O _ _
        // _ _ _
        val board = MutableList(9) { CellState.EMPTY }
        board[0] = CellState.X
        board[1] = CellState.X
        board[3] = CellState.O
        val state = GameState(
            board = board,
            phase = GamePhase.AI_THINKING,
            aiDifficulty = AiDifficulty.MEDIUM
        )
        val result = GameEngine.makeAiMove(state)
        // Medium AI should block at index 2
        assertEquals(CellState.O, result.board[2])
    }

    // ---- Local Multiplayer Tests ----

    @Test
    fun `local multiplayer alternates X and O`() {
        val state = GameState(
            phase = GamePhase.PLAYER_TURN,
            gameMode = GameMode.LOCAL_MULTIPLAYER,
            currentSymbol = CellState.X
        )
        val afterX = GameEngine.makeLocalMove(state, 0)
        assertNotNull(afterX)
        assertEquals(CellState.X, afterX!!.board[0])
        assertEquals(GamePhase.OPPONENT_TURN, afterX.phase)
        assertEquals(CellState.O, afterX.currentSymbol)

        val afterO = GameEngine.makeLocalMove(afterX, 4)
        assertNotNull(afterO)
        assertEquals(CellState.O, afterO!!.board[4])
        assertEquals(GamePhase.PLAYER_TURN, afterO.phase)
        assertEquals(CellState.X, afterO.currentSymbol)
    }

    // ---- Variable Board Size Tests ----

    @Test
    fun `5x5 board win combinations are generated`() {
        val config = BoardConfig(size = 5, winLength = 5)
        val combos = GameEngine.getWinCombinations(config)
        assertTrue(combos.isNotEmpty())
        // Each combo should have exactly 5 elements
        assertTrue(combos.all { it.size == 5 })
    }

    @Test
    fun `5x5 board horizontal win detected`() {
        val config = BoardConfig(size = 5, winLength = 5)
        val board = MutableList(25) { CellState.EMPTY }
        // Fill first row with X
        for (i in 0..4) board[i] = CellState.X
        val winning = GameEngine.getWinningLine(board, CellState.X, config)
        assertNotNull(winning)
        assertEquals(listOf(0, 1, 2, 3, 4), winning)
    }
}
