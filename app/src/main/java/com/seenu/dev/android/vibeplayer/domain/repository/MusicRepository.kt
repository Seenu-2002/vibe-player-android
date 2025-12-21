package com.seenu.dev.android.vibeplayer.domain.repository

import com.seenu.dev.android.vibeplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getAllScannedTracksFlow(): Flow<List<Track>>

    suspend fun getTrackById(trackId: Long): Track?

    suspend fun updateScannedMusicList(musicList: List<Track>)

    suspend fun scan(
        minDurationSeconds: Long,
        minFileSizeInKb: Long
    ): List<Track>

}