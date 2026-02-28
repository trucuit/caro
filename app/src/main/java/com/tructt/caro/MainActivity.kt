package com.tructt.caro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tructt.caro.presentation.GameViewModel
import com.tructt.caro.ui.screens.GameScreen
import com.tructt.caro.ui.screens.MenuScreen
import com.tructt.caro.ui.theme.CaroTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaroTheme {
                val showMenu by viewModel.showMenu.collectAsStateWithLifecycle()

                if (showMenu) {
                    MenuScreen(
                        onStartGame = { mode, difficulty, boardSize ->
                            viewModel.startGame(mode, difficulty, boardSize)
                        }
                    )
                } else {
                    GameScreen(viewModel = viewModel)
                }
            }
        }
    }
}
