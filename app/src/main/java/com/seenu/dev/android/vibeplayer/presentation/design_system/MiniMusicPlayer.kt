package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.R.attr.track
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.surfaceHigher

@Preview
@Composable
private fun MiniMusicPlayerPreview() {
    VibePlayerTheme {
        SharedTransitionScope {
            MiniMusicPlayer(
                isPlaying = false,
                track = TrackUiModel(
                    id = 1L,
                    mediaId = 1L,
                    albumId = 1L,
                    name = "Song Title",
                    artistName = "Artist Name",
                    duration = 215000L,
                    durationLabel = "3:35",
                    filePath = ""
                ),
                onPlayPause = {},
                onNext = {},
                currentPos = 0L,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun SharedTransitionScope.MiniMusicPlayer(
    isPlaying: Boolean,
    currentPos: Long,
    track: TrackUiModel,
    onPlayPause: (Boolean) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TrackImage(
            track = track, modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.small)
//                .sharedElement(
//                    sharedContentState = rememberSharedContentState(
//                        "track_image(${track.id})"
//                    ),
//                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
//                    boundsTransform = { _, _ ->
//                        tween(500)
//                    }
//                )
        )

        Column(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = track.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth()
//                            .sharedElement(
//                                sharedContentState = rememberSharedContentState(
//                                    "track_title(${track.id})"
//                                ),
//                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
//                                boundsTransform = { _, _ ->
//                                    tween(500)
//                                }
//                            )
                        ,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = track.artistName,
                        style = MaterialTheme.typography.bodyMediumRegular,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.fillMaxWidth()
//                            .sharedElement(
//                                sharedContentState = rememberSharedContentState(
//                                    "track_artist(${track.id})"
//                                ),
//                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
//                                boundsTransform = { _, _ ->
//                                    tween(500)
//                                }
//                            )
                        ,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                "player_play_pause"
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            boundsTransform = { _, _ ->
                                tween(500)
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
                        contentDescription = desc,
                        modifier = Modifier.size(16.dp)
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
                                tween(500)
                            }
                        ),
                    onClick = {
                        onNext()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
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

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "player_seekbar"
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = { _, _ ->
                            tween(500)
                        }
                    ),
                progress = { currentPos / track.duration.toFloat() },
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(.15F),
                gapSize = (-4).dp,
                drawStopIndicator = {}
            )
        }

    }
}