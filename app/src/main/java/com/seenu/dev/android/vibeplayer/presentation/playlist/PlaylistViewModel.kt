package com.seenu.dev.android.vibeplayer.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.model.PlaylistWithSongsCount
import com.seenu.dev.android.vibeplayer.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PlaylistViewModel constructor(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PlaylistScreenUiState> = MutableStateFlow(
        PlaylistScreenUiState()
    )
    val uiState: StateFlow<PlaylistScreenUiState> = _uiState
        .onStart {
            getPlaylists()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

    private fun getPlaylists() {
        viewModelScope.launch {
            getPlaylistsUseCase().collect { playlists ->
                _uiState.value = _uiState.value.copy(
                    playlists = playlists
                )
            }
        }
    }

}

data class PlaylistScreenUiState constructor(
    val favouriteSongsCount: Int = 0,
    val playlists: List<PlaylistWithSongsCount> = emptyList()
)

sealed interface PlaylistScreenIntent {
    data class CreatePlaylist(val name: String) : PlaylistScreenIntent
}