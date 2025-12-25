package com.seenu.dev.android.vibeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.rememberNavBackStack
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.LocalDimensions
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.mobileDimensions
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.tabletDimensions
import com.seenu.dev.android.vibeplayer.presentation.navigation.Route
import com.seenu.dev.android.vibeplayer.presentation.navigation.VibePlayerNavigation
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.utils.PermissionUtils

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val route: Route = if (PermissionUtils().hasRequiredPermissions(this)) {
            Route.MusicList
        } else {
            Route.Permission
        }
        setContent {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isTablet = this.maxWidth >= 840.dp
                val dimension = if (isTablet) {
                    tabletDimensions
                } else {
                    mobileDimensions
                }
                CompositionLocalProvider(LocalDimensions provides dimension) {
                    VibePlayerTheme {
                        VibePlayerNavigation(route)
                    }
                }
            }
        }
    }
}