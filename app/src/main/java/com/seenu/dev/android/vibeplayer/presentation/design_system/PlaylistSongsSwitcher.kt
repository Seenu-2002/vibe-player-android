package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumMedium

@Preview
@Composable
private fun PlaylistSongsSwitcherPreview() {
    VibePlayerTheme {
        PlaylistSongsSwitcher(
            selected = PlaylistSongsSwitcherTab.entries.random(),
            onSelected = {}
        )
    }
}

enum class PlaylistSongsSwitcherTab {
    PLAYLISTS,
    SONGS
}

@Composable
fun PlaylistSongsSwitcher(
    selected: PlaylistSongsSwitcherTab,
    onSelected: (PlaylistSongsSwitcherTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderLineColor = MaterialTheme.colorScheme.outline
    Column(
        modifier = modifier
            .drawBehind {
                val borderHeight = 1.dp.toPx()
                val height = size.height
                this.drawLine(
                    color = borderLineColor,
                    start = Offset(0F, height - borderHeight / 2F),
                    end = Offset(size.width, height - borderHeight / 2F),
                    strokeWidth = borderHeight
                )
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onSelected(PlaylistSongsSwitcherTab.SONGS)
                    }, contentAlignment = Alignment.Center
            ) {
                SwitchTab(
                    text = stringResource(R.string.songs),
                    isSelected = selected == PlaylistSongsSwitcherTab.SONGS,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onSelected(PlaylistSongsSwitcherTab.SONGS)
                    }, contentAlignment = Alignment.Center
            ) {
                SwitchTab(
                    text = stringResource(R.string.playlist),
                    isSelected = selected == PlaylistSongsSwitcherTab.PLAYLISTS,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SwitchTabPreview() {
    VibePlayerTheme {
        SwitchTab(
            text = "Playlist",
            isSelected = true,
        )
    }
}

@Composable
fun SwitchTab(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    val borderColor = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .drawBehind {
                if (isSelected) {
                    val width = size.width
                    val height = size.height
                    val borderHeight = 4.dp.toPx()

                    val path = Path()
                    path.moveTo(0F, height)
                    path.cubicTo(
                        0F, height,
                        0F, height - borderHeight,
                        borderHeight, height - borderHeight
                    )
                    path.lineTo(
                        width - borderHeight,
                        height - borderHeight
                    )
                    path.cubicTo(
                        width - borderHeight, height - borderHeight,
                        width, height - borderHeight,
                        width, height
                    )
                    path.lineTo(width, height)
                    path.lineTo(0F, height)
                    path.close()

                    drawPath(path, SolidColor(borderColor))
                }

            }
            .padding(vertical = 12.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMediumMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}