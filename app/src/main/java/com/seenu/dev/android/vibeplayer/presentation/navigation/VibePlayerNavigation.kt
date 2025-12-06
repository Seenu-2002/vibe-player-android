package com.seenu.dev.android.vibeplayer.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.seenu.dev.android.vibeplayer.presentation.music_list.MusicListScreen
import kotlin.collections.removeLastOrNull

@Composable
fun VibePlayerNavigation(backstack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backstack,
        modifier = Modifier.fillMaxSize(),
        onBack = { backstack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.MusicList> {
                MusicListScreen()
            }
            entry<Route.MusicPlayer> {
                TODO()
            }
        }
    )
}