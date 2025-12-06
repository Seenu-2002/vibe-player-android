package com.seenu.dev.android.vibeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.rememberNavBackStack
import com.seenu.dev.android.vibeplayer.presentation.navigation.Route
import com.seenu.dev.android.vibeplayer.presentation.navigation.VibePlayerNavigation
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VibePlayerTheme {
                val backstack = rememberNavBackStack(
                    Route.MusicList
                )
                VibePlayerNavigation(backstack)
            }
        }
    }
}