package com.seenu.dev.android.vibeplayer.presentation.utils

fun Long.toDurationLabel(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}