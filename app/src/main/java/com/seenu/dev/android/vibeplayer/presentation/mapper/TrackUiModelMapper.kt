package com.seenu.dev.android.vibeplayer.presentation.mapper

import android.text.format.DateUtils
import com.seenu.dev.android.vibeplayer.domain.model.Track
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel

fun Track.toUiModel(): TrackUiModel {
    return TrackUiModel(
        id = this.id,
        mediaId = this.mediaId,
        name = this.name,
        artistName = this.artist ?: "<unknown>",
        duration = this.duration,
        durationLabel = DateUtils.formatElapsedTime(
            this.duration / 1000
        ),
        albumId = this.albumId,
        filePath = this.filePath,
    )
}