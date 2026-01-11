package com.seenu.dev.android.vibeplayer.domain.model

data class Playlist constructor(
    val id: Long,
    val name: String
)

data class PlaylistWithSongsCount(
    val playlist: Playlist,
    val songsCount: Int
)