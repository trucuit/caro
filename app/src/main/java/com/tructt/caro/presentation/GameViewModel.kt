package com.tructt.caro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tructt.caro.domain.AiDifficulty
import com.tructt.caro.domain.BoardConfig
import com.tructt.caro.domain.CellState
import com.tructt.caro.domain.GameEngine
import com.tructt.caro.domain.GameMode
import com.tructt.caro.domain.GamePhase
import com.tructt.caro.domain.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    // Whether we're showing the menu or playing
    private val _showMenu = MutableStateFlow(true)
    val showMenu: StateFlow<Boolean> = _showMenu.asStateFlow()

    fun startGame(
        mode: GameMode,
        difficulty: AiDifficulty = AiDifficulty.HARD,
        boardSize: Int = 3
    ) {
        val winLength = if (boardSize <= 3) 3 else 5
        val config = BoardConfig(size = boardSize, winLength = winLength)

        _showMenu.value = false
        viewModelScope.launch {
            _state.value = GameState(
                board = List(config.totalCells) { CellState.EMPTY },
                phase = GamePhase.LOADING,
                gameMode = mode,
                aiDifficulty = difficulty,
                boardConfig = config
            )
            delay(800) // Simulate loading
            _state.value = _state.value.copy(phase = GamePhase.PLAYER_TURN)
        }
    }

    fun goToMenu() {
        _showMenu.value = true
    }

    fun onCellClick(index: Int) {
        val current = _state.value

        when (current.gameMode) {
            GameMode.VS_AI -> handleAiModeClick(current, index)
            GameMode.LOCAL_MULTIPLAYER -> handleLocalClick(current, index)
        }
    }

    private fun handleAiModeClick(current: GameState, index: Int) {
        if (current.phase != GamePhase.PLAYER_TURN) return

        val afterPlayerMove = GameEngine.makePlayerMove(current, index) ?: return
        _state.value = afterPlayerMove

        if (afterPlayerMove.phase != GamePhase.AI_THINKING) return

        viewModelScope.launch {
            delay(700) // Simulate AI thinking
            val afterAiMove = GameEngine.makeAiMove(_state.value)
            _state.value = afterAiMove
        }
    }

    private fun handleLocalClick(current: GameState, index: Int) {
        if (current.phase != GamePhase.PLAYER_TURN && current.phase != GamePhase.OPPONENT_TURN) return

        val afterMove = GameEngine.makeLocalMove(current, index) ?: return
        _state.value = afterMove
    }

    fun onBackPressed() {
        val current = _state.value
        if (current.phase == GamePhase.PLAYER_TURN ||
            current.phase == GamePhase.AI_THINKING ||
            current.phase == GamePhase.OPPONENT_TURN
        ) {
            _state.value = current.copy(showLeaveDialog = true)
        }
    }

    fun onConfirmLeave() {
        // BRD 3.4: leaving a match counts as a loss
        _state.value = _state.value.copy(phase = GamePhase.LOST, showLeaveDialog = false)
    }

    fun onCancelLeave() {
        _state.value = _state.value.copy(showLeaveDialog = false)
    }

    fun playAgain() {
        val current = _state.value
        startGame(
            mode = current.gameMode,
            difficulty = current.aiDifficulty,
            boardSize = current.boardConfig.size
        )
    }
}
