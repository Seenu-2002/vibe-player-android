package com.seenu.dev.android.vibeplayer.presentation.shared_vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class ScanResultViewModel : ViewModel() {

    private val _scanResult: Channel<Int> = Channel(
        capacity = Channel.BUFFERED
    )
    val scanResult: Flow<Int> = _scanResult.receiveAsFlow()

    fun publishResult(scannedSongsCount: Int) {
        viewModelScope.launch {
            Timber.d("Publishing scan result: $scannedSongsCount")
            _scanResult.send(scannedSongsCount)
        }
    }

}