package com.seenu.dev.android.vibeplayer.presentation.music_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.audio.AudioPlayer
import com.seenu.dev.android.vibeplayer.domain.audio.PlaybackState
import com.seenu.dev.android.vibeplayer.domain.usecase.GetTrackUseCase
import com.seenu.dev.android.vibeplayer.presentation.mapper.toUiModel
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MusicPlayerViewModel constructor(
    private val audioPlayer: AudioPlayer,
    private val getTrackUseCase: GetTrackUseCase
) : ViewModel() {

    private val _musicPlayerUiState: MutableStateFlow<MusicPlayerUiState> =
        MutableStateFlow(MusicPlayerUiState())
    val musicPlayerUiState: StateFlow<MusicPlayerUiState> = _musicPlayerUiState.asStateFlow()

    init {
        viewModelScope.launch {
            observerPlayerState()
        }
    }

    fun onIntent(intent: MusicPlayerIntent) {
        viewModelScope.launch {
            when (intent) {
                is MusicPlayerIntent.PrepareAndPlay -> {
                    val track = getTrackUseCase(intent.trackId)
                    if (track == null) {
                        _musicPlayerUiState.emit(
                            MusicPlayerUiState(
                                isMusicNotFound = true
                            )
                        )
                    } else {
                        val path = track.filePath
                        audioPlayer.setSource(path)
                        audioPlayer.play()
                        trackCurrentTrackPosition()
                        _musicPlayerUiState.emit(
                            MusicPlayerUiState(
                                isTrackLoaded = true,
                                isPlaying = true,
                                durationMs = track.duration,
                                track = track.toUiModel()
                            )
                        )
                    }
                }

                MusicPlayerIntent.Play -> {
                    audioPlayer.play()
                }

                MusicPlayerIntent.Pause -> {
                    audioPlayer.pause()
                }

                is MusicPlayerIntent.Seek -> {
                    audioPlayer.seekTo(intent.to)
                }
            }
        }
    }

    private var observerJob: Job? = null
    private fun trackCurrentTrackPosition() {
        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            while (isActive) {
                val currentPosition = audioPlayer.currentPositionMs
                _musicPlayerUiState.emit(
                    musicPlayerUiState.value.copy(
                        currentPositionMs = currentPosition
                    )
                )
                delay(100L)
            }
        }
    }

    private suspend fun observerPlayerState() {
        audioPlayer.playbackState.collectLatest {
            when (it) {
                PlaybackState.Playing -> {
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            isPlaying = true
                        )
                    )
                }
                PlaybackState.Paused -> {
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            isPlaying = false
                        )
                    )
                }
            }
        }
    }

    override fun onCleared() {
        audioPlayer.pause()
        super.onCleared()
    }
}

data class MusicPlayerUiState(
    val isTrackLoaded: Boolean = false,
    val isPlaying: Boolean = false,
    val isMusicNotFound: Boolean = false,
    val currentPositionMs: Long = 0L,
    val track: TrackUiModel? = null,
    val durationMs: Long = 0L
)

sealed interface MusicPlayerIntent {
    data class PrepareAndPlay constructor(val trackId: Long) : MusicPlayerIntent
    data object Play : MusicPlayerIntent
    data object Pause : MusicPlayerIntent
    data class Seek constructor(val to: Long) : MusicPlayerIntent
}