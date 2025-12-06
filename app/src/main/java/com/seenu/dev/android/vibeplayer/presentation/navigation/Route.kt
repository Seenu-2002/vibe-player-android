package com.seenu.dev.android.vibeplayer.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable

    data object MusicList : Route

    @Serializable
    data class MusicPlayer(val musicId: Long) : Route

}