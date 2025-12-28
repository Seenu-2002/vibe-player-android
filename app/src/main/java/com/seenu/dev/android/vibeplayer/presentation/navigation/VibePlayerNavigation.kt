package com.seenu.dev.android.vibeplayer.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.seenu.dev.android.vibeplayer.presentation.music_list.MusicListScreen
import com.seenu.dev.android.vibeplayer.presentation.music_player.MusicPlayerScreen
import com.seenu.dev.android.vibeplayer.presentation.permission.PermissionScreen
import com.seenu.dev.android.vibeplayer.presentation.scan_music_config.ScanMusicScreen
import kotlin.collections.removeLastOrNull

@Composable
fun VibePlayerNavigation(startRoute: Route) {
    SharedTransitionLayout {
        val backstack = rememberNavBackStack(startRoute)
        NavDisplay(
            backStack = backstack,
            modifier = Modifier.fillMaxSize(),
            onBack = { backstack.removeLastOrNull() },
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            sharedTransitionScope = this,
            entryProvider = { key ->
                when (key) {
                    Route.Permission -> {
                        NavEntry(key) {
                            PermissionScreen(onAllowed = {
                                backstack.clear()
                                backstack.add(Route.MusicList)
                            })
                        }
                    }

                    Route.MusicList -> {
                        NavEntry(key) {
                            MusicListScreen(onPlay = { track ->
                                val route = Route.MusicPlayer(track.id)
                                backstack.add(route)
                            }, onScanMusic = {
                                backstack.add(Route.ScanMusic)
                            })
                        }
                    }

                    is Route.MusicPlayer -> {
                        NavEntry(key) {
                            MusicPlayerScreen(trackId = key.trackId, onNavigateUp = {
                                backstack.removeLastOrNull()
                            })
                        }
                    }

                    Route.ScanMusic -> {
                        NavEntry(key) {
                            ScanMusicScreen(
                                onScanCompleted = {
                                    backstack.removeLastOrNull()
                                },
                                onNavigateUp = {
                                    backstack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    else -> error("Unknown NavKey: $key")
                }
            }
        )
    }
}