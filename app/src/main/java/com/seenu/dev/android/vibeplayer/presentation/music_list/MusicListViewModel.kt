package com.seenu.dev.android.vibeplayer.presentation.music_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import com.seenu.dev.android.vibeplayer.domain.usecase.GetScanConfigUseCase
import com.seenu.dev.android.vibeplayer.domain.usecase.LoadMediaItemsToPlayerUseCase
import com.seenu.dev.android.vibeplayer.domain.usecase.MarkInitialTrackScanCompletedUseCase
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicConfig
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicInDiskUseCase
import com.seenu.dev.android.vibeplayer.presentation.mapper.toUiModel
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class MusicListViewModel constructor(
    private val repository: MusicRepository,
    private val scanMusicUseCase: ScanMusicInDiskUseCase,
    private val loadMediaItemsToPlayerUseCase: LoadMediaItemsToPlayerUseCase,
    private val getScanConfigUseCase: GetScanConfigUseCase,
    private val markInitialTrackScanCompletedUseCase: MarkInitialTrackScanCompletedUseCase
) : ViewModel() {

    val musicListUiState: StateFlow<MusicListUiState> = combine(
        getScanConfigUseCase(),
        repository.getAllScannedTracksFlow()
    ) { scanConfig, tracks ->
        loadMediaItemsToPlayerUseCase(tracks)
        val musicList = tracks.map { musicEntity ->
            musicEntity.toUiModel()
        }
        MusicListUiState(
            isLoading = false,
            isScanning = false,
            musicList = musicList,
            scanConfig = scanConfig
        )
    }.catch { e ->
        Timber.e(e, "Error fetching music list")
        emit(
            MusicListUiState(
                isLoading = false,
                musicList = emptyList()
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MusicListUiState(
            isLoading = true
        )
    )

    fun onIntent(intent: MusicListIntent) {
        viewModelScope.launch {
            when (intent) {
                MusicListIntent.ScanMusicInDisk -> {
                    val config = ScanMusicConfig(
                        minDurationInSeconds = 30,
                        minSizeInKb = 500
                    )
                    delay(500) // To show loading state
                    markInitialTrackScanCompletedUseCase()
                    scanMusicUseCase(
                        config = config
                    )
                }
            }
        }
    }

}

data class MusicListUiState constructor(
    val isLoading: Boolean = false,
    val isScanning: Boolean = false,
    val musicList: List<TrackUiModel> = emptyList(),
    val scanConfig: ScanConfig = ScanConfig()
)

sealed interface MusicListIntent {
    data object ScanMusicInDisk : MusicListIntent
}