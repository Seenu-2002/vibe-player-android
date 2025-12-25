package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.content.ContentUris
import android.provider.MediaStore
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel

@Composable
fun TrackImage(
    track: TrackUiModel?,
    modifier: Modifier = Modifier,
    placeholderPadding: PaddingValues = PaddingValues(14.dp)
) {
    val context = LocalContext.current
    val albumArtUri = remember(track) {
        if (track == null) {
            return@remember null
        }

        if (track.albumId > 0) {
            ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                track.albumId
            )
        } else null
    }

    if (albumArtUri != null) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(albumArtUri)
                .crossfade(true)
                .build(),
            error = {
                MusicDefaultImageThumbnail(
                    modifier = modifier,
                    padding = placeholderPadding
                )
            },
            contentDescription = null,
            modifier = modifier
        )
    } else {
        MusicDefaultImageThumbnail(
            modifier = modifier,
            padding = placeholderPadding
        )
    }
}