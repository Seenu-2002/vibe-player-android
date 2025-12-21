package com.seenu.dev.android.vibeplayer.presentation.design_system

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular

@Preview
@Composable
private fun MusicListCardPreview() {
    VibePlayerTheme {
        val music = TrackUiModel(
            id = 1L,
            name = "Song Title",
            artistName = "Artist Name",
            duration = 215000L,
            durationLabel = "3:35",
            filePath = ""
        )
        MusicListCard(music = music, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun MusicListCard(music: TrackUiModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MusicDefaultImageThumbnail()
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(12.dp), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = music.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = music.artistName,
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = music.durationLabel,
            style = MaterialTheme.typography.bodyMediumRegular,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun MusicDefaultImageThumbnail(modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.accent
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accent.copy(alpha = .14F),
                        accent.copy(alpha = .028F),
                    )
                ),
                shape = MaterialTheme.shapes.small
            )
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_music_placeholder),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
    }
}