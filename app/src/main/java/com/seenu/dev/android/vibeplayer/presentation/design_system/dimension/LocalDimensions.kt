package com.seenu.dev.android.vibeplayer.presentation.design_system.dimension

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

val LocalDimensions = compositionLocalOf<Dimension> { error("No dimension provided") }

val mobileDimensions = Dimension(
    musicListScreenDimensions = MusicListScreenDimensions(
        horizontalPadding = 16.dp,
    ),
    musicPlayerScreenDimensions = MusicPlayerScreenDimensions(
        horizontalPadding = 8.dp,
    ),
)

val tabletDimensions = Dimension(
    musicListScreenDimensions = MusicListScreenDimensions(
        horizontalPadding = 24.dp,
    ),
    musicPlayerScreenDimensions = MusicPlayerScreenDimensions(
        horizontalPadding = 24.dp,
    ),
)