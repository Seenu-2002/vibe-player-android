package com.seenu.dev.android.vibeplayer.domain.audio

import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow

interface AudioPlayer {

    val playbackState: Flow<PlaybackState>

    val currentPositionMs: Long

    fun play()
    fun playTrack(index: Int)
    fun pause()
    fun seekTo(positionMs: Long)
    fun setSource(path: String)
    fun hasNext(): Boolean
    fun hasPrevious(): Boolean
    fun playNext()
    fun playPrevious()
    fun currentTrackIndex(): Int
    fun clearTracks()
    fun loadTracks(paths: List<String>, startIndex: Int = 0)
    fun getLoadedTracks(): List<MediaStore.Audio.Media>
    fun release()

}

sealed interface PlaybackState {
    data object Playing : PlaybackState
    data object Paused : PlaybackState
    data object TrackChange : PlaybackState
    data object SeekbarJump : PlaybackState
}