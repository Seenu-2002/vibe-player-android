package com.seenu.dev.android.vibeplayer.presentation.design_system

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium

@Preview
@Composable
private fun ShuffleAndPlayButtonRowPreview() {
    VibePlayerTheme {
        ShuffleAndPlayButtonRow(
            totalSongsCount = 42,
            onShuffleAndPlay = {},
            onPlay = {}
        )
    }
}

@Composable
fun ShuffleAndPlayButtonRow(
    totalSongsCount: Int,
    onShuffleAndPlay: () -> Unit = {},
    onPlay: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VibePlayerButton(
                iconRes = R.drawable.ic_shuffle,
                text = stringResource(R.string.shuffle),
                onClick = onShuffleAndPlay,
                modifier = Modifier
                    .weight(1f)
            )

            VibePlayerButton(
                iconRes = R.drawable.ic_play_outline,
                text = stringResource(R.string.play),
                onClick = onPlay,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.songs_count, totalSongsCount),
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
