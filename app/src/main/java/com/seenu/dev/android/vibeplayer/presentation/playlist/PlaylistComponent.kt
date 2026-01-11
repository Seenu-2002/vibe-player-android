package com.seenu.dev.android.vibeplayer.presentation.playlist

import android.R.attr.contentDescription
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.convertByteToUUID
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.domain.model.PlaylistWithSongsCount
import com.seenu.dev.android.vibeplayer.presentation.design_system.dimension.LocalDimensions
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinApplication

@Preview
@Composable
private fun PlaylistComponentPreview() {
    VibePlayerTheme {
        PlaylistComponent(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun PlaylistComponent(modifier: Modifier = Modifier) {
    val viewModel: PlaylistViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = pluralStringResource(
                    R.plurals.playlist_quantity,
                    count = uiState.playlists.size,
                    uiState.playlists.size
                ),
                style = MaterialTheme.typography.bodyLargeMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.buttonHover,
                        shape = CircleShape
                    )
                    .clickable {
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = stringResource(id = R.string.add_playlist)
                )
            }
        }

        FavouritesPlaylistRow(
            songsCount = uiState.favouriteSongsCount,
            modifier = Modifier.fillMaxWidth()
        )

        PlaylistList(playlists = uiState.playlists)
    }
}

@Preview
@Composable
private fun FavouritesPlaylistRowPreview() {
    VibePlayerTheme {
        FavouritesPlaylistRow(10, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun FavouritesPlaylistRow(songsCount: Int, modifier: Modifier = Modifier) {
    PlaylistCard(
        name = stringResource(R.string.favourites),
        songsCount = songsCount,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                tint = Color.Unspecified
            )
        },
        modifier = modifier,
    )
}

@Composable
fun ColumnScope.PlaylistList(
    playlists: List<PlaylistWithSongsCount>,
    onPlaylistClick: (Long) -> Unit = {}
) {
    Text(
        text = stringResource(R.string.playlist_count, 12),
        style = MaterialTheme.typography.bodyLargeMedium,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp, vertical = 8.dp
            )
    )

    val horizontalPadding = LocalDimensions.current.musicListScreenDimensions.horizontalPadding
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
    ) {
        items(playlists.size, key = { index ->
            playlists[index].playlist.id
        }) { index ->
            val playlist = playlists[index]

            PlaylistCard(
                name = playlist.playlist.name,
                songsCount = playlist.songsCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding / 2, vertical = 6.dp)
                    .clip(
                        MaterialTheme.shapes.medium
                    )
                    .clickable {
                        onPlaylistClick(playlist.playlist.id)
                    }
                    .padding(horizontal = horizontalPadding / 2, vertical = 6.dp)
            )

            if (index != playlists.lastIndex) {
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

@Composable
fun CreatePlaylistButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        modifier = modifier.border(
            width = 1.dp,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.outline
        ), onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = stringResource(R.string.create_playlist),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.create_playlist),
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun PlaylistCard(
    name: String,
    songsCount: Int,
    icon: @Composable (BoxScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = .14F),
                            MaterialTheme.colorScheme.primary.copy(alpha = .028F)
                        )
                    ),
                    shape = CircleShape
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            icon?.invoke(this) ?: Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.song_quantity,
                    count = songsCount,
                    songsCount
                ),
                style = MaterialTheme.typography.bodyLargeRegular,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Unspecified
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu_dots),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}