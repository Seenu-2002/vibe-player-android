package com.seenu.dev.android.vibeplayer.presentation.search

import android.R.attr.contentDescription
import android.R.attr.text
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.design_system.MusicListCard
import com.seenu.dev.android.vibeplayer.presentation.music_list.MusicListContent
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun SharedTransitionScope.TrackSearchScreen(onNavigateUp: () -> Unit) {
    val viewModel: TrackSearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrackSearchBar(
                text = uiState.query,
                onValueChange = { query ->
                    viewModel.onIntent(TrackSearchEvent.OnQueryChange(query))
                },
                onClear = {
                    viewModel.onIntent(TrackSearchEvent.OnQueryChange(""))
                },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1F)
            )

            TextButton(onClick = onNavigateUp) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.buttonPrimary
                )
            }
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.query.isNotEmpty() && uiState.searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_results_found),
                        style = MaterialTheme.typography.bodyMediumRegular,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            else -> {
                val tracks = uiState.searchResults
                MusicListContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    tracks = tracks,
                    onClicked = {

                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrackSearchBarPreview() {
    VibePlayerTheme {
        TrackSearchBar(
            modifier = Modifier.fillMaxWidth(),
            text = "",
            onValueChange = {},
            onClear = {},
        )
    }
}

@Composable
fun TrackSearchBar(
    text: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.buttonHover,
                shape = CircleShape
            )
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "Search tracks",
            tint = MaterialTheme.colorScheme.onSecondary
        )

        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .wrapContentHeight()
                .weight(1F)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLargeRegular.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = MaterialTheme.typography.bodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    innerTextField()
                }
            },
        )

        if (text.isNotEmpty()) {

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Clear search",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .clickable(onClick = onClear)
            )
        }
    }

}