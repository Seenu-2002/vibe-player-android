package com.seenu.dev.android.vibeplayer.domain.audio

import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow

interface AudioPlayer {

    val playbackState: Flow<PlaybackState>

    val currentPositionMs: Long

    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setSource(path: String)

    fun release()

}

sealed interface PlaybackState {
    data object Playing : PlaybackState
    data object Paused : PlaybackState
}