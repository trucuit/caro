package com.tructt.caro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tructt.caro.presentation.GameViewModel
import com.tructt.caro.ui.navigation.CaroNavHost
import com.tructt.caro.ui.theme.CaroTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaroTheme {
                CaroNavHost(viewModel = viewModel)
            }
        }
    }
}
