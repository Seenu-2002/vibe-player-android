package com.seenu.dev.android.vibeplayer.presentation.music_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.MusicListCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.NoMusicFoundCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.ScanningMusicCard
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListScreen(onPlay: (TrackUiModel) -> Unit) {

    val viewModel: MusicListViewModel = koinViewModel()
    val uiState by viewModel.musicListUiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_music),
                            contentDescription = "Music Icon",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.accent
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.vibe_player),
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = MaterialTheme.colorScheme.accent
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.buttonHover,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_scan),
                            contentDescription = "Scan Icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isScanning -> {
                    ScanningMusicCard(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                else -> {
                    if (uiState.musicList.isEmpty()) {
                        NoMusicFoundCard(
                            onScanAgain = {
                                viewModel.onIntent(
                                    MusicListIntent.ScanMusicInDisk
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        MusicListContent(
                            music = uiState.musicList,
                            onClicked = {
                                onPlay(it)
                            },
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicListContent(
    music: List<TrackUiModel>,
    onClicked: (TrackUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(music.size, key = {
            music[it].id
        }) { index ->
            val musicItem = music[index]
            MusicListCard(
                music = musicItem,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .clip(
                        MaterialTheme.shapes.medium
                    )
                    .clickable {
                        onClicked(musicItem)
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )
            if (index != music.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}