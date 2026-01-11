package com.seenu.dev.android.vibeplayer.data.mapper

import com.seenu.dev.android.vibeplayer.data.model.PlaylistEntity
import com.seenu.dev.android.vibeplayer.data.model.PlaylistWithSongsCountEntity
import com.seenu.dev.android.vibeplayer.domain.model.Playlist
import com.seenu.dev.android.vibeplayer.domain.model.PlaylistWithSongsCount

fun PlaylistEntity.toDomain() = Playlist(
    id = this.playlistId,
    name = this.name
)

fun Playlist.toEntity() = PlaylistEntity(
    playlistId = this.id,
    name = this.name
)

fun PlaylistWithSongsCountEntity.toDomain() = PlaylistWithSongsCount(
    playlist = this.playlistEntity.toDomain(),
    songsCount = this.songCount
)

fun PlaylistWithSongsCount.toEntity() = PlaylistWithSongsCountEntity(
    playlistEntity = this.playlist.toEntity(),
    songCount = this.songsCount
)