package com.seenu.dev.android.vibeplayer.presentation.music_list

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.AnimationScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.MiniMusicPlayerScaffold
import com.seenu.dev.android.vibeplayer.presentation.design_system.MusicListCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.NoMusicFoundCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.ScanningMusicCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.ShuffleAndPlayButtonRow
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.LocalDimensions
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import com.seenu.dev.android.vibeplayer.presentation.music_player.MusicPlayerIntent
import com.seenu.dev.android.vibeplayer.presentation.music_player.MusicPlayerViewModel
import com.seenu.dev.android.vibeplayer.presentation.shared_vm.ScanResultViewModel
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonPrimary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel
import timber.log.Timber

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.MusicListScreen(
    onPlay: (TrackUiModel) -> Unit,
    onScanMusic: () -> Unit,
    onSearch: () -> Unit,
) {

    val viewModel: MusicListViewModel = koinViewModel()
    val playerViewModel: MusicPlayerViewModel = koinActivityViewModel()
    val scanResultViewModel: ScanResultViewModel = koinActivityViewModel()
    val uiState by viewModel.musicListUiState.collectAsStateWithLifecycle()

    val trackListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollToTopButton by remember {
        derivedStateOf {
            trackListState.firstVisibleItemIndex > 10
        }
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    Timber.d("Shared view model instance: $scanResultViewModel")

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        scanResultViewModel.scanResult.collectLatest { count ->
            Timber.d("New scan result received: $count")
            snackBarHostState.showSnackbar(
                message = context.getString(
                    R.string.scan_results_message,
                    count
                )
            )
        }
    }

    LaunchedEffect(uiState) {
        if (!uiState.scanConfig.isInitialScanDone) {
            viewModel.onIntent(MusicListIntent.ScanMusicInDisk)
        }
    }

    MiniMusicPlayerScaffold(
        modifier = Modifier.fillMaxSize(),
        viewModel = playerViewModel,
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
                        onClick = onScanMusic,
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
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onSearch,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.buttonHover,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            trackListState.scrollToItem(0)
                        }
                    },
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.buttonPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_up),
                        contentDescription = "Scroll to Top"
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isScanning -> {
                    ScanningMusicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }

                else -> {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.accent
                        )
                    } else if (uiState.musicList.isEmpty()) {
                        NoMusicFoundCard(
                            onScanAgain = {
                                viewModel.onIntent(
                                    MusicListIntent.ScanMusicInDisk
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column(modifier = Modifier.matchParentSize()) {
                            ShuffleAndPlayButtonRow(
                                totalSongsCount = uiState.musicList.size,
                                onShuffleAndPlay = {
                                    playerViewModel.onIntent(MusicPlayerIntent.EnableShuffleAndPlay)
                                },
                                onPlay = {
                                    playerViewModel.onIntent(MusicPlayerIntent.RevertShuffleAndPlayFromStart)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                            MusicListContent(
                                tracks = uiState.musicList,
                                onClicked = {
                                    onPlay(it)
                                },
                                listState = trackListState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SharedTransitionScope.MusicListContent(
    tracks: List<TrackUiModel>,
    listState: LazyListState = rememberLazyListState(),
    onClicked: (TrackUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, state = listState) {
        items(tracks.size, key = {
            tracks[it].id
        }) { index ->
            val musicItem = tracks[index]
            val horizontalPadding =
                LocalDimensions.current.musicListScreenDimensions.horizontalPadding
            MusicListCard(
                track = musicItem,
                modifier = Modifier
                    .padding(horizontal = horizontalPadding / 2, vertical = 6.dp)
                    .clip(
                        MaterialTheme.shapes.medium
                    )
                    .clickable {
                        onClicked(musicItem)
                    }
                    .padding(horizontal = horizontalPadding / 2, vertical = 6.dp)
            )
            if (index != tracks.lastIndex) {
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