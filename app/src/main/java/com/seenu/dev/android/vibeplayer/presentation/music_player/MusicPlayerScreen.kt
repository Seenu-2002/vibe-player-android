package com.seenu.dev.android.vibeplayer.presentation.music_player

import android.R.attr.track
import androidx.activity.compose.BackHandler
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.MiniMusicPlayerScaffold
import com.seenu.dev.android.vibeplayer.presentation.design_system.TrackImage
import com.seenu.dev.android.vibeplayer.presentation.design_system.VibePlayerNavIconButton
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.LocalDimensions
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.bodySmallRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import com.seenu.dev.android.vibeplayer.presentation.theme.textDisabled
import com.seenu.dev.android.vibeplayer.presentation.utils.toDurationLabel
import org.koin.compose.viewmodel.koinActivityViewModel
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun MusicPlayerScreenPreview() {
    VibePlayerTheme {
        SharedTransitionScope {
            MusicPlayerScreen(trackId = 121L, onNavigateUp = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.MusicPlayerScreen(trackId: Long, onNavigateUp: () -> Unit) {
    val viewModel: MusicPlayerViewModel = koinActivityViewModel()
    val uiState by viewModel.musicPlayerUiState.collectAsStateWithLifecycle()
    val trackState = uiState.trackState as? TrackUiState.Found?

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
                        iconRes = R.drawable.ic_chevron_down,
                        onClick = {
                            onNavigateUp()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        val horizontalPadding =
            LocalDimensions.current.musicPlayerScreenDimensions.horizontalPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 12.dp, horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
            TrackImage(
                track = trackState?.track,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth()
                    .aspectRatio(1F)
                    .clip(MaterialTheme.shapes.small)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "track_image(${trackId})"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
                placeholderPadding = PaddingValues(
                    70.dp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = trackState?.track?.name ?: "<unknown>",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "track_title(${trackId})"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = trackState?.track?.artistName ?: "<unknown>",
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "track_artist(${trackId})"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
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
                currentDuration = trackState?.currentPositionMs ?: 0L,
                totalDuration = trackState?.track?.duration ?: 1L,
                isPlaying = uiState.isPlaying,
                hasNext = uiState.hasNext,
                hasPrevious = uiState.hasPrevious,
                onPlayPause = {
                    viewModel.onIntent(
                        if (it) MusicPlayerIntent.Play else MusicPlayerIntent.Pause
                    )
                },
                onNext = {
                    viewModel.onIntent(MusicPlayerIntent.Next)
                },
                onPrevious = {
                    viewModel.onIntent(MusicPlayerIntent.Previous)
                },
                onSeekTo = {
                    viewModel.onIntent(MusicPlayerIntent.Seek(to = it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.PlayerSeekBar(
    currentDuration: Long,
    totalDuration: Long,
    isPlaying: Boolean,
    hasNext: Boolean,
    hasPrevious: Boolean,
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
        Slider(
            modifier = Modifier.fillMaxWidth()
                .sharedElement(
                    sharedContentState = rememberSharedContentState(
                        "player_seekbar"
                    ),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    boundsTransform = { _, _ ->
                        tween(1000)
                    }
                ),
            value = currentDuration.toFloat(),
            onValueChange = {
                onSeekTo(it.toLong())
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onPrimary,
                activeTrackColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTrackColor = MaterialTheme.colorScheme.outline,
            ),
            thumb = {
                val current = currentDuration.toDurationLabel()
                val total = totalDuration.toDurationLabel()
                Text(
                    text = "$current / $total",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.bodySmallRegular,
                    modifier = Modifier
                        .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                color = Color(0x4D0A131D),
                                radius = 4.dp,
                            )
                        )
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = CircleShape
                        )
                        .padding(horizontal = 4.dp)
                )
            },
            track = { state ->
                SliderDefaults.Track(
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.onPrimary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    sliderState = state,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                )
            },
            valueRange = 0F..totalDuration.toFloat(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "player_previous"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
                enabled = hasPrevious,
                onClick = onPrevious,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.buttonHover,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.buttonHover.copy(.28F),
                    disabledContentColor = MaterialTheme.colorScheme.textDisabled
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_skip_previous),
                    contentDescription = "Previous",
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                modifier = Modifier.size(60.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "player_play_pause"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
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
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "player_next"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(1000)
                        }
                    ),
                enabled = hasNext,
                onClick = onNext,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.buttonHover,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.buttonHover.copy(.28F),
                    disabledContentColor = MaterialTheme.colorScheme.textDisabled
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