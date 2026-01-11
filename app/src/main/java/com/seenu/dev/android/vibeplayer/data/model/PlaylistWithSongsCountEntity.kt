package com.seenu.dev.android.vibeplayer.data.model

import androidx.room.Embedded

data class PlaylistWithSongsCountEntity constructor(
    @Embedded
    val playlistEntity: PlaylistEntity,
    val songCount: Int
)