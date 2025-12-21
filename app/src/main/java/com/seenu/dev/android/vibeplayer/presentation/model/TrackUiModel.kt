package com.seenu.dev.android.vibeplayer.presentation.model

data class TrackUiModel constructor(
    val id: Long,
    val name: String,
    val artistName: String,
    val duration: Long,
    val durationLabel: String,
    val filePath: String
)