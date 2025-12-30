package com.seenu.dev.android.vibeplayer.presentation.music_player

import android.R.attr.track
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.audio.AudioPlayer
import com.seenu.dev.android.vibeplayer.domain.audio.PlaybackState
import com.seenu.dev.android.vibeplayer.domain.model.RepeatMode
import com.seenu.dev.android.vibeplayer.domain.usecase.GetAllTracksUseCase
import com.seenu.dev.android.vibeplayer.presentation.mapper.toUiModel
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.utils.findWithIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class MusicPlayerViewModel constructor(
    private val audioPlayer: AudioPlayer,
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {

    private val _musicPlayerUiState: MutableStateFlow<MusicPlayerUiState> =
        MutableStateFlow(createUiState())
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
                    val tracks = getAllTracksUseCase()
                    if (tracks.isEmpty()) {
                        _musicPlayerUiState.emit(
                            MusicPlayerUiState(
                                isMusicNotFound = true
                            )
                        )
                    } else {
                        val trackWithIndex = tracks.findWithIndex {
                            it.id == intent.trackId
                        }

                        if (trackWithIndex == null) {
                            _musicPlayerUiState.emit(
                                createUiState().copy(
                                    tracks = tracks.map { it.toUiModel() },
                                    isMusicNotFound = true
                                )
                            )
                        } else {

                            val (index, track) = trackWithIndex

                            if (audioPlayer.currentTrackIndex() == index) {
                                return@launch
                            }
                            audioPlayer.playTrack(
                                index
                            )
                            trackCurrentTrackPosition()
                            _musicPlayerUiState.emit(
                                createUiState().copy(
                                    isTrackLoaded = true,
                                    isPlaying = true,
                                    tracks = tracks.map { it.toUiModel() },
                                    trackState = TrackUiState.Found(
                                        track = track.toUiModel(),
                                        durationMs = track.duration,
                                        currentPositionMs = 0L
                                    ),
                                    hasNext = audioPlayer.hasNext(),
                                    hasPrevious = audioPlayer.hasPrevious(),
                                )
                            )
                        }
                    }
                }

                MusicPlayerIntent.Play -> {
                    audioPlayer.play()
                }

                MusicPlayerIntent.Pause -> {
                    audioPlayer.pause()
                }

                MusicPlayerIntent.Next -> {
                    audioPlayer.playNext()
                }

                MusicPlayerIntent.Previous -> {
                    audioPlayer.playPrevious()
                }

                is MusicPlayerIntent.Seek -> {
                    audioPlayer.seekTo(intent.to)
                }

                is MusicPlayerIntent.Shuffle -> {
                    audioPlayer.shuffle(intent.enable)
                }

                is MusicPlayerIntent.ChangeRepeatMode -> {
                    audioPlayer.changeRepeatMode(intent.mode)
                }

                MusicPlayerIntent.EnableShuffleAndPlay -> {
                    shuffleAndPlay()
                }

                MusicPlayerIntent.RevertShuffleAndPlayFromStart -> {
                    revertShuffleAndPlayFirst()
                }
            }
        }
    }

    private suspend fun revertShuffleAndPlayFirst() {

        val tracks = if (!_musicPlayerUiState.value.isTrackLoaded) {
            getAllTracksUseCase().map { it.toUiModel() }
        } else {
            musicPlayerUiState.value.tracks
        }

        val track = tracks.firstOrNull() ?: return
        _musicPlayerUiState.emit(
            createUiState().copy(
                isTrackLoaded = true,
                isPlaying = true,
                tracks = tracks,
                trackState = TrackUiState.Found(
                    track = track,
                    durationMs = track.duration,
                    currentPositionMs = 0L
                ),
                hasNext = audioPlayer.hasNext(),
                hasPrevious = audioPlayer.hasPrevious(),
            )
        )
        audioPlayer.shuffle(false)
        audioPlayer.playTrack(0)
    }

    private suspend fun shuffleAndPlay() {
        val tracks = if (!_musicPlayerUiState.value.isTrackLoaded) {
            val tracks = getAllTracksUseCase().map { it.toUiModel() }
            _musicPlayerUiState.emit(
                createUiState().copy(
                    isTrackLoaded = true,
                    isPlaying = true,
                    tracks = tracks,
                    hasNext = audioPlayer.hasNext(),
                    hasPrevious = audioPlayer.hasPrevious(),
                )
            )
            tracks
        } else _musicPlayerUiState.value.tracks
        audioPlayer.shuffle(true)
        audioPlayer.playTrack(
            tracks.indices.random()
        )
    }

    private suspend fun updateCurrentTrack() {
        val index = audioPlayer.currentTrackIndex()
        val track = musicPlayerUiState.value.tracks.getOrNull(index)
        if (track == null) {
            _musicPlayerUiState.emit(
                _musicPlayerUiState.value.copy(
                    trackState = TrackUiState.NotFound,
                    hasNext = audioPlayer.hasNext(),
                    hasPrevious = audioPlayer.hasPrevious()
                )
            )
        } else {
            _musicPlayerUiState.emit(
                _musicPlayerUiState.value.copy(
                    trackState = TrackUiState.Found(
                        track = track,
                        durationMs = track.duration,
                        currentPositionMs = 0L
                    ),
                    hasNext = audioPlayer.hasNext(),
                    hasPrevious = audioPlayer.hasPrevious()
                )
            )
        }
    }

    private fun createUiState(): MusicPlayerUiState {
        val isShuffled = audioPlayer.isShuffleEnabled
        val repeatMode = audioPlayer.repeatMode
        return MusicPlayerUiState(
            isShuffleEnabled = isShuffled,
            repeatMode = repeatMode,
            hasNext = audioPlayer.hasNext(),
            hasPrevious = audioPlayer.hasPrevious(),
        )
    }

    private var observerJob: Job? = null
    private fun trackCurrentTrackPosition() {
        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            while (isActive) {
                val currentPosition = audioPlayer.currentPositionMs
                val trackState = musicPlayerUiState.value.trackState
                if (trackState is TrackUiState.Found) {
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            trackState = trackState.copy(
                                currentPositionMs = currentPosition
                            )
                        )
                    )
                }
                delay(100L)
            }
        }
    }

    private suspend fun observerPlayerState() {
        audioPlayer.playbackState.collectLatest {
            when (it) {
                PlaybackState.Playing -> {
                    val trackIndex = audioPlayer.currentTrackIndex()
                    val track = musicPlayerUiState.value.tracks.getOrNull(trackIndex)
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            isPlaying = true,
                            trackState = TrackUiState.Found(
                                track = track,
                                durationMs = track?.duration ?: 0L,
                                currentPositionMs = audioPlayer.currentPositionMs
                            )
                        )
                    )
                }

                PlaybackState.Paused -> {
                    val trackIndex = audioPlayer.currentTrackIndex()
                    val track = musicPlayerUiState.value.tracks.getOrNull(trackIndex)
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            isPlaying = false,
                            trackState = TrackUiState.Found(
                                track = track,
                                durationMs = track?.duration ?: 0L,
                                currentPositionMs = audioPlayer.currentPositionMs
                            )
                        )
                    )
                }

                PlaybackState.SeekbarJump -> {
                    val trackState =
                        if (musicPlayerUiState.value.trackState is TrackUiState.Found) {
                            val currentTrackIndex = audioPlayer.currentTrackIndex()
                            val track = musicPlayerUiState.value.tracks.getOrNull(currentTrackIndex)
                            if (track != null) {
                                TrackUiState.Found(
                                    track = track,
                                    durationMs = track.duration,
                                    currentPositionMs = audioPlayer.currentPositionMs
                                )
                            } else {
                                TrackUiState.NotFound
                            }
                        } else {
                            musicPlayerUiState.value.trackState
                        }
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            trackState = trackState
                        )
                    )
                }

                PlaybackState.TrackChange -> {
                    updateCurrentTrack()
                }

                PlaybackState.ShuffleModeChange -> {
                    val isShuffled = audioPlayer.isShuffleEnabled
                    Timber.d("Shuffle mode changed: $isShuffled")
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            isShuffleEnabled = isShuffled
                        )
                    )
                }

                PlaybackState.RepeatModeChange -> {
                    val repeatMode = audioPlayer.repeatMode
                    Timber.d("Repeat mode changed: $repeatMode")
                    _musicPlayerUiState.emit(
                        musicPlayerUiState.value.copy(
                            repeatMode = repeatMode
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

data class MusicPlayerUiState constructor(
    val isTrackLoaded: Boolean = false,
    val isPlaying: Boolean = false,
    val isMusicNotFound: Boolean = false,
    val tracks: List<TrackUiModel> = emptyList(),
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val trackState: TrackUiState = TrackUiState.Idle,
)

sealed interface TrackUiState {
    data class Found constructor(
        val track: TrackUiModel? = null,
        val durationMs: Long = 0L,
        val currentPositionMs: Long = 0L,
    ) : TrackUiState

    data object NotFound : TrackUiState

    data object Idle : TrackUiState
}


sealed interface MusicPlayerIntent {
    data class PrepareAndPlay constructor(val trackId: Long) : MusicPlayerIntent
    data object Play : MusicPlayerIntent
    data object Pause : MusicPlayerIntent
    data object Next : MusicPlayerIntent
    data object Previous : MusicPlayerIntent
    data class Seek constructor(val to: Long) : MusicPlayerIntent
    data class Shuffle constructor(val enable: Boolean) : MusicPlayerIntent
    data class ChangeRepeatMode constructor(val mode: com.seenu.dev.android.vibeplayer.domain.model.RepeatMode) :
        MusicPlayerIntent

    data object EnableShuffleAndPlay : MusicPlayerIntent
    data object RevertShuffleAndPlayFromStart : MusicPlayerIntent

}