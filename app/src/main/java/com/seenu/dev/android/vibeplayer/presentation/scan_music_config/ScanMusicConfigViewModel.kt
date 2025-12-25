package com.seenu.dev.android.vibeplayer.presentation.scan_music_config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.domain.usecase.GetScanConfigUseCase
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicConfig
import com.seenu.dev.android.vibeplayer.domain.usecase.ScanMusicInDiskUseCase
import com.seenu.dev.android.vibeplayer.domain.usecase.UpdateScanConfigsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ScanMusicViewModel constructor(
    private val scanMusicUseCase: ScanMusicInDiskUseCase,
    private val getScanConfigUseCase: GetScanConfigUseCase,
    private val updateScanConfigUseCase: UpdateScanConfigsUseCase
) : ViewModel() {

    private val _scanMusicState: MutableStateFlow<ScanMusicUiState> =
        MutableStateFlow(ScanMusicUiState())
    val scanMusicState: StateFlow<ScanMusicUiState> = _scanMusicState
        .onStart {
            getScanConfigs()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScanMusicUiState()
        )

    private val _scanResultState: MutableSharedFlow<ScanResultState> = MutableSharedFlow()
    val scanResultState: SharedFlow<ScanResultState> = _scanResultState.asSharedFlow()

    private fun getScanConfigs() {
        viewModelScope.launch {
            getScanConfigUseCase().collectLatest {
                _scanMusicState.value = _scanMusicState.value.copy(scanConfig = it)
            }
        }
    }

    fun onIntent(intent: ScanMusicIntent) {
        viewModelScope.launch {
            when (intent) {
                is ScanMusicIntent.Scan -> {
                    _scanMusicState.value =
                        _scanMusicState.value.copy(scanState = ScanState.Scanning)
                    val count = scanMusic(
                        size = _scanMusicState.value.scanConfig.minSize,
                        duration = _scanMusicState.value.scanConfig.minDuration
                    )
                    updateScanConfigUseCase(
                        scanConfig = _scanMusicState.value.scanConfig
                    )
                    _scanResultState.emit(ScanResultState(scannedSongsCount = count))
                    _scanMusicState.value =
                        _scanMusicState.value.copy(scanState = ScanState.Idle)
                }

                is ScanMusicIntent.UpdateSize -> {
                    _scanMusicState.value = _scanMusicState.value.copy(
                        scanConfig = _scanMusicState.value.scanConfig.copy(
                            minSize = intent.size
                        )
                    )
                }

                is ScanMusicIntent.UpdateDuration -> {
                    _scanMusicState.value = _scanMusicState.value.copy(
                        scanConfig = _scanMusicState.value.scanConfig.copy(
                            minDuration = intent.duration
                        )
                    )
                }
            }
        }
    }

    private suspend fun scanMusic(size: ScanConfig.MinSize, duration: ScanConfig.MinDuration): Int {
        val config = ScanMusicConfig(
            minSizeInKb = size.value,
            minDurationInSeconds = duration.value
        )
        return scanMusicUseCase(
            config
        )
    }

}

data class ScanMusicUiState constructor(
    val scanState: ScanState = ScanState.Idle,
    val scanConfig: ScanConfig = ScanConfig(
        minSize = ScanConfig.MinSize.KB_100,
        minDuration = ScanConfig.MinDuration.SEC_30
    )
)

data class ScanResultState constructor(
    val scannedSongsCount: Int
)

sealed interface ScanState {
    data object Idle : ScanState
    data object Scanning : ScanState
}

sealed interface ScanMusicIntent {
    data object Scan : ScanMusicIntent
    data class UpdateSize(val size: ScanConfig.MinSize) : ScanMusicIntent
    data class UpdateDuration(val duration: ScanConfig.MinDuration) : ScanMusicIntent
}