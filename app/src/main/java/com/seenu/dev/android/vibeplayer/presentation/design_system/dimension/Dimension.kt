package com.seenu.dev.android.vibeplayer.presentation.design_system.dimension

import androidx.compose.ui.unit.Dp

data class Dimension constructor(
    val musicListScreenDimensions: MusicListScreenDimensions,
    val musicPlayerScreenDimensions: MusicPlayerScreenDimensions,
)

data class MusicListScreenDimensions constructor(
    val horizontalPadding: Dp,
)

data class MusicPlayerScreenDimensions constructor(
    val horizontalPadding: Dp,
)