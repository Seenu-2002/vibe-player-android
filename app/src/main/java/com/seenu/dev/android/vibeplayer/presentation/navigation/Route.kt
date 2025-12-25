package com.seenu.dev.android.vibeplayer.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.seenu.dev.android.vibeplayer.presentation.model.TrackUiModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable

    data object Permission : Route

    @Serializable

    data object MusicList : Route


    @Serializable
    data class MusicPlayer constructor(val trackId: Long) : Route

    @Serializable

    data object ScanMusic : Route

}