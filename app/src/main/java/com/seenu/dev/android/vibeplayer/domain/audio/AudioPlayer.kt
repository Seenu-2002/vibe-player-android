package com.seenu.dev.android.vibeplayer.domain.audio

import android.provider.MediaStore
import com.seenu.dev.android.vibeplayer.domain.model.RepeatMode
import kotlinx.coroutines.flow.Flow

interface AudioPlayer {

    val playbackState: Flow<PlaybackState>

    val currentPositionMs: Long
    val isShuffleEnabled: Boolean
    val repeatMode: RepeatMode

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
    fun shuffle(enable: Boolean)
    fun changeRepeatMode(repeatMode: RepeatMode)
    fun release()

}

sealed interface PlaybackState {
    data object Playing : PlaybackState
    data object Paused : PlaybackState
    data object TrackChange : PlaybackState
    data object SeekbarJump : PlaybackState
    data object ShuffleModeChange : PlaybackState
    data object RepeatModeChange : PlaybackState
}