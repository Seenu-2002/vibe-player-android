package com.seenu.dev.android.vibeplayer.data.mapper

import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import com.seenu.dev.android.vibeplayer.domain.model.Track
import java.io.File

fun MusicTrackEntity.toDomain(): Track {
    return Track(
        id = this.trackId,
        mediaId = this.musicId,
        name = this.name,
        filePath = path,
        artist = this.artist,
        album = this.album,
        size = this.size,
        albumId = this.albumId,
        duration = this.duration
    )
}

fun Track.toEntity(): MusicTrackEntity {
    return MusicTrackEntity(
        trackId = this.id,
        musicId = this.mediaId,
        name = this.name,
        path = this.filePath,
        artist = this.artist,
        albumId = this.albumId,
        album = this.album,
        size = this.size,
        duration = this.duration
    )
}