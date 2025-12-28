package com.seenu.dev.android.vibeplayer.presentation.design_system

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.presentation.music_player.MusicPlayerIntent
import com.seenu.dev.android.vibeplayer.presentation.music_player.MusicPlayerViewModel
import com.seenu.dev.android.vibeplayer.presentation.music_player.TrackUiState
import com.seenu.dev.android.vibeplayer.presentation.theme.surfaceHigher
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun SharedTransitionScope.MiniMusicPlayerScaffold(
    topBar: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (Boolean) -> Unit,
) {
    val viewModel: MusicPlayerViewModel = koinActivityViewModel()
    val trackUiState by viewModel.musicPlayerUiState.collectAsStateWithLifecycle()

    val trackState = trackUiState.trackState as? TrackUiState.Found?
    val showMiniPlayer =
        trackUiState.isTrackLoaded && trackState != null && trackState.track != null

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        snackbarHost = snackbarHost,
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                content(showMiniPlayer)
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                ) {
                    floatingActionButton()
                }
            }
            AnimatedVisibility(showMiniPlayer) {
                val trackState = trackState!!
                val track = trackState.track!!
                MiniMusicPlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceHigher,
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        ),
                    track = track,
                    isPlaying = trackUiState.isPlaying,
                    currentPos = trackState.currentPositionMs,
                    onPlayPause = {
                        viewModel.onIntent(
                            if (trackUiState.isPlaying) {
                                MusicPlayerIntent.Pause
                            } else {
                                MusicPlayerIntent.Play
                            }
                        )
                    },
                    onNext = {
                        viewModel.onIntent(MusicPlayerIntent.Next)
                    }
                )
            }
        }
    }
}