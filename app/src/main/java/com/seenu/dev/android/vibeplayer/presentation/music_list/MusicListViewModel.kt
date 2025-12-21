package com.seenu.dev.android.vibeplayer.presentation.music_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicConfig
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicInDiskUseCase
import com.seenu.dev.android.vibeplayer.presentation.mapper.toUiModel
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class MusicListViewModel constructor(
    private val repository: MusicRepository,
    private val scanMusicUseCase: ScanMusicInDiskUseCase
) : ViewModel() {

    private val _musicListUiState: MutableStateFlow<MusicListUiState> =
        MutableStateFlow(MusicListUiState())
    val musicListUiState: StateFlow<MusicListUiState> = _musicListUiState
        .onStart {
            fetchMusicList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MusicListUiState()
        )

    private fun fetchMusicList() {
        viewModelScope.launch {
            _musicListUiState.value = _musicListUiState.value.copy(isScanning = true)
            try {
                repository.getAllScannedTracksFlow().collectLatest {
                    val musicList = it.map { musicEntity ->
                        musicEntity.toUiModel()
                    }
                    _musicListUiState.value = MusicListUiState(
                        isScanning = false,
                        musicList = musicList
                    )

                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching music list")
                _musicListUiState.value = MusicListUiState(
                    isScanning = false,
                    musicList = emptyList()
                )
            }
        }
    }

    fun onIntent(intent: MusicListIntent) {
        viewModelScope.launch {
            when (intent) {
                MusicListIntent.ScanMusicInDisk -> {
                    val config = ScanMusicConfig(
                        minDurationInSeconds = 30,
                        minSizeInKb = 500
                    )
                    scanMusicUseCase(
                        config = config
                    )
                }
            }
        }
    }

}

data class MusicListUiState constructor(
    val isScanning: Boolean = false,
    val musicList: List<TrackUiModel> = emptyList(),
)

sealed interface MusicListIntent {
    data object ScanMusicInDisk : MusicListIntent
}