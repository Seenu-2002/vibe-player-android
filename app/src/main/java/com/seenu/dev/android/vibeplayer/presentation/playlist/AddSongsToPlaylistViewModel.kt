package com.seenu.dev.android.vibeplayer.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.model.Track
import com.seenu.dev.android.vibeplayer.domain.usecase.SearchTracksByNameOrArtistUseCase
import com.seenu.dev.android.vibeplayer.presentation.mapper.toUiModel
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AddSongsToPlaylistViewModel constructor(
    private val searchTracksUseCase: SearchTracksByNameOrArtistUseCase,
    private val playlistId: Long
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddSongsUiState> = MutableStateFlow(AddSongsUiState())
    val uiState: StateFlow<AddSongsUiState> = _uiState.asStateFlow()

    private val searchQuery: MutableSharedFlow<String> = MutableSharedFlow()

    init {
        observeSearchQuery()
    }

    fun onIntent(intent: AddSongsIntent) {
        when (intent) {
            is AddSongsIntent.OnSearchQueryChange -> {
                _uiState.value = _uiState.value.copy(searchQuery = intent.query, isLoading = true)
            }

            is AddSongsIntent.OnSongSelected -> {
                val currentSelected = _uiState.value.selectedSongIds.toMutableSet()
                currentSelected.add(intent.songId)
                _uiState.value = _uiState.value.copy(selectedSongIds = currentSelected)
            }

            is AddSongsIntent.OnSongDeselected -> {
                val currentSelected = _uiState.value.selectedSongIds.toMutableSet()
                currentSelected.remove(intent.songId)
                _uiState.value = _uiState.value.copy(selectedSongIds = currentSelected)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeSearchQuery() {
        searchQuery
            .distinctUntilChanged()
            .debounce(400)
            .flatMapLatest { query ->
                flow {
                    if (query.isBlank()) {
                        emit(emptyList())
                        return@flow
                    }

                    val result = searchTracksUseCase(query)
                    emit(result)
                }
            }
            .onEach { tracks ->
                _uiState.value = _uiState.value.copy(
                    tracks = tracks.map(Track::toUiModel),
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

}

data class AddSongsUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val tracks: List<TrackUiModel> = emptyList(),
    val selectedSongIds: Set<Long> = emptySet()
)

sealed interface AddSongsIntent {
    data class OnSearchQueryChange(val query: String) : AddSongsIntent
    data class OnSongSelected(val songId: Long) : AddSongsIntent
    data class OnSongDeselected(val songId: Long) : AddSongsIntent
}