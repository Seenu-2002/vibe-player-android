package com.seenu.dev.android.vibeplayer.presentation.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.MusicListCardSelectionMode
import com.seenu.dev.android.vibeplayer.presentation.design_system.VibePlayerNavIconButton
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.LocalDimensions
import com.seenu.dev.android.vibeplayer.presentation.search.TrackSearchBar
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongsToPlaylistScreen(playlistId: Long, onBackPress: () -> Unit) {
    val viewModel: AddSongsToPlaylistViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val horizontalPadding =
        LocalDimensions.current.musicListScreenDimensions.horizontalPadding

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            val title = if (uiState.tracks.isEmpty()) {
                stringResource(R.string.add_songs)
            } else {
                pluralStringResource(
                    R.plurals.selected,
                    uiState.tracks.size,
                    uiState.tracks.size
                )
            }
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    VibePlayerNavIconButton(
                        onClick = onBackPress
                    )
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TrackSearchBar(
                text = uiState.searchQuery,
                onValueChange = { query ->
                    viewModel.onIntent(AddSongsIntent.OnSearchQueryChange(query))
                },
                onClear = {
                    viewModel.onIntent(AddSongsIntent.OnSearchQueryChange(""))
                },
                modifier = Modifier.fillMaxWidth(1F)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(modifier = Modifier.matchParentSize()) {
                        items(uiState.tracks.size, key = { index -> uiState.tracks[index].id }) {
                            val item = uiState.tracks[it]
                            val isSelected = uiState.selectedSongIds.contains(item.id)

                            MusicListCardSelectionMode(
                                selected = isSelected,
                                track = item,
                                onClick = {
                                    viewModel.onIntent(
                                        if (isSelected) {
                                            AddSongsIntent.OnSongDeselected(item.id)
                                        } else {
                                            AddSongsIntent.OnSongSelected(item.id)
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .padding(
                                        horizontal = horizontalPadding / 2,
                                        vertical = 6.dp
                                    )
                                    .clip(
                                        MaterialTheme.shapes.medium
                                    )
                                    .clickable {
                                        viewModel.onIntent(
                                            if (isSelected) {
                                                AddSongsIntent.OnSongDeselected(item.id)
                                            } else {
                                                AddSongsIntent.OnSongSelected(item.id)
                                            }
                                        )
                                    }
                                    .padding(horizontal = horizontalPadding / 2, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}