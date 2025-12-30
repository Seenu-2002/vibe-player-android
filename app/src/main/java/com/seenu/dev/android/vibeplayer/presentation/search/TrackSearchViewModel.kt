package com.seenu.dev.android.vibeplayer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrackSearchViewModel constructor(
    private val searchUseCase: SearchTracksByNameOrArtistUseCase
) : ViewModel() {

    private val searchQuery: MutableSharedFlow<String> = MutableSharedFlow()

    private val _uiState: MutableStateFlow<TrackSearchUiState> =
        MutableStateFlow(TrackSearchUiState())
    val uiState: StateFlow<TrackSearchUiState> = _uiState.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onIntent(event: TrackSearchEvent) {
        viewModelScope.launch {
            when (event) {
                is TrackSearchEvent.OnQueryChange -> {
                    val query = event.query.trim()
                    _uiState.value = _uiState.value.copy(query = query, isLoading = true)
                    searchQuery.emit(query)
                }
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

                    val result = searchUseCase(query)
                    emit(result)
                }
            }
            .onEach { tracks ->
                _uiState.value = _uiState.value.copy(
                    searchResults = tracks.map { it.toUiModel() },
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

}

sealed interface TrackSearchEvent {
    data class OnQueryChange(val query: String) : TrackSearchEvent
}

data class TrackSearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<TrackUiModel> = emptyList()
)