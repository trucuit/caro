package com.tructt.caro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tructt.caro.presentation.GameViewModel
import com.tructt.caro.ui.screens.*

/**
 * Navigation routes for the Caro app.
 */
sealed class Screen(val route: String) {
    data object Loading : Screen("loading")
    data object Menu : Screen("menu")
    data object Game : Screen("game")
    data object Victory : Screen("victory")
    data object Defeat : Screen("defeat")
    data object Draw : Screen("draw")
    data object Cancelled : Screen("cancelled")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
    data object History : Screen("history")
}

@Composable
fun CaroNavHost(
    viewModel: GameViewModel,
    navController: NavHostController = rememberNavController()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route
    ) {
        composable(Screen.Loading.route) {
            LoadingScreen(
                onLoadingComplete = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Menu.route) {
            MenuScreen(
                onStartGame = { mode, difficulty, boardSize ->
                    viewModel.startGame(mode, difficulty, boardSize)
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Menu.route) { inclusive = false }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                viewModel = viewModel,
                onGameEnd = { phase ->
                    val dest = when (phase) {
                        com.tructt.caro.domain.GamePhase.WON -> Screen.Victory.route
                        com.tructt.caro.domain.GamePhase.LOST -> Screen.Defeat.route
                        com.tructt.caro.domain.GamePhase.DRAW -> Screen.Draw.route
                        com.tructt.caro.domain.GamePhase.CANCELLED -> Screen.Cancelled.route
                        else -> return@GameScreen
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Game.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Victory.route) {
            OutcomeScreen(
                state = state,
                onPlayAgain = {
                    viewModel.playAgain()
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Menu.route) { inclusive = false }
                    }
                },
                onMainMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Defeat.route) {
            OutcomeScreen(
                state = state,
                onPlayAgain = {
                    viewModel.playAgain()
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Menu.route) { inclusive = false }
                    }
                },
                onMainMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Draw.route) {
            OutcomeScreen(
                state = state,
                onPlayAgain = {
                    viewModel.playAgain()
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Menu.route) { inclusive = false }
                    }
                },
                onMainMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Cancelled.route) {
            CancelledScreen(
                onGoBack = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
