package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeRegular
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import com.seenu.dev.android.vibeplayer.presentation.theme.surfaceHigher

@Preview
@Composable
private fun CreatePlaylistBottomSheetPreview() {
    VibePlayerTheme {
        CreatePlaylistBottomSheet(
            onDismissRequest = {},
            playlistName = "My Playlist",
            onNameChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistBottomSheet(
    onDismissRequest: () -> Unit,
    playlistName: String,
    onNameChange: (String) -> Unit,
    maxLength: Int = 40,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = {},
        modifier = modifier,
        dragHandle = {},
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.create_playlist),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.buttonHover
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .padding(
                        vertical = 12.dp, horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = playlistName,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onNameChange(it)
                        }
                    },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (playlistName.isBlank()) {
                            Box(modifier = Modifier) {
                                Text(
                                    text = stringResource(R.string.enter_playlist_placeholder),
                                    style = MaterialTheme.typography.bodyLargeRegular,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                        innerTextField()
                    },
                    textStyle = MaterialTheme.typography.bodyLargeRegular.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
                )

                Text(
                    text = "${playlistName.length}/$maxLength",
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VibePlayerButton(
                    iconRes = null,
                    text = stringResource(R.string.cancel),
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                )
                VibePlayerPrimaryButton(
                    text = stringResource(R.string.create),
                    enabled = playlistName.isNotBlank(),
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}