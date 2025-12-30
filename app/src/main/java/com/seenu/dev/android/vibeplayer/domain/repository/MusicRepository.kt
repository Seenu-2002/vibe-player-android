package com.seenu.dev.android.vibeplayer.domain.repository

import com.seenu.dev.android.vibeplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getAllScannedTracksFlow(): Flow<List<Track>>

    suspend fun getAllScannedTracks(): List<Track>

    suspend fun getScannedTracks(offset: Int, count: Int): List<Track>

    suspend fun getTrackById(trackId: Long): Track?

    suspend fun searchTracksByNameOrArtist(query: String): List<Track>

    suspend fun updateScannedMusicList(musicList: List<Track>)

    suspend fun scan(
        minDurationSeconds: Long,
        minFileSizeInKb: Long
    ): List<Track>

    suspend fun deleteTrackByIds(vararg trackId: Long)

}