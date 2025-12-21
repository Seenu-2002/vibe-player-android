package com.seenu.dev.android.vibeplayer.presentation.music_player

import android.R.attr.track
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.VibePlayerNavIconButton
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun MusicPlayerScreenPreview() {
    VibePlayerTheme {
        val track = TrackUiModel(
            id = 1L,
            name = "505",
            artistName = "Arctic Monkeys",
            duration = 215000L,
            durationLabel = "3:35",
            filePath = ""
        )
        MusicPlayerScreen(trackId = 121L, onNavigateUp = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(trackId: Long, onNavigateUp: () -> Unit) {
    val viewModel: MusicPlayerViewModel = koinViewModel()
    val uiState by viewModel.musicPlayerUiState.collectAsStateWithLifecycle()

    LaunchedEffect(trackId) {
        viewModel.onIntent(
            MusicPlayerIntent.PrepareAndPlay(
                trackId = trackId
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    VibePlayerNavIconButton(
                        onClick = onNavigateUp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
            val accent = MaterialTheme.colorScheme.accent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1F)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accent.copy(alpha = .14F),
                                accent.copy(alpha = .028F),
                            )
                        ),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(70.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_music_placeholder),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.track?.name ?: "<unknown>",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.track?.artistName ?: "<unknown>",
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
            PlayerSeekBar(
                currentDuration = uiState.currentPositionMs,
                totalDuration = uiState.track?.duration ?: 1L,
                isPlaying = uiState.isPlaying,
                onPlayPause = {
                    viewModel.onIntent(
                        if (it) MusicPlayerIntent.Play else MusicPlayerIntent.Pause
                    )
                },
                onNext = { /*TODO*/ },
                onPrevious = { /*TODO*/ },
                onSeekTo = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PlayerSeekBar(
    currentDuration: Long,
    totalDuration: Long,
    isPlaying: Boolean,
    onPlayPause: (Boolean) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = {
                if (totalDuration == 0L) {
                    0F
                } else {
                    currentDuration / totalDuration.toFloat()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            drawStopIndicator = {},
            gapSize = (-10).dp,
            trackColor = MaterialTheme.colorScheme.outline,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevious,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.buttonHover,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_skip_previous),
                    contentDescription = "Previous",
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    onPlayPause(!isPlaying)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                val (icon, desc) = if (!isPlaying) {
                    R.drawable.ic_play to "Play"
                } else {
                    R.drawable.ic_pause to "Pause"
                }
                Icon(
                    painter = painterResource(icon),
                    contentDescription = desc
                )
            }

            IconButton(
                onClick = onNext,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.buttonHover,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_skip_next),
                    contentDescription = "Next",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}