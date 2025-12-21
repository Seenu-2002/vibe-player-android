package com.seenu.dev.android.vibeplayer.domain.model

data class Track constructor(
    val id: Long,
    val mediaId: Long,
    val name: String,
    val filePath: String,
    val artist: String?,
    val album: String?,
    val size: Long,
    val duration: Long
)