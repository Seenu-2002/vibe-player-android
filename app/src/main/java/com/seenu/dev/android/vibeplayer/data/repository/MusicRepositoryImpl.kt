package com.seenu.dev.android.vibeplayer.data.repository

import com.seenu.dev.android.vibeplayer.data.local.VibePlayerDatabase
import com.seenu.dev.android.vibeplayer.data.mapper.toDomain
import com.seenu.dev.android.vibeplayer.data.mapper.toEntity
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import com.seenu.dev.android.vibeplayer.data.source.LocalMusicScanner
import com.seenu.dev.android.vibeplayer.domain.model.PlaylistWithSongsCount
import com.seenu.dev.android.vibeplayer.domain.model.Track
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single(binds = [MusicRepository::class])
class MusicRepositoryImpl constructor(
    private val database: VibePlayerDatabase,
    private val localMusicScanner: LocalMusicScanner
) : MusicRepository {

    override fun getAllScannedTracksFlow(): Flow<List<Track>> {
        return database.musicDao.getAllScannedTracksFlow()
            .map { list -> list.map(MusicTrackEntity::toDomain) }
    }

    override suspend fun getAllScannedTracks(): List<Track> {
        return database.musicDao.getScannedTracks().map { it.toDomain() }
    }

    override suspend fun getScannedTracks(offset: Int, count: Int): List<Track> {
        return database.musicDao.getScannedTracks(offset, count).map { it.toDomain() }
    }

    override suspend fun getTrackById(trackId: Long): Track? {
        return database.musicDao.getTrackById(trackId)?.toDomain()
    }

    override suspend fun searchTracksByNameOrArtist(query: String): List<Track> {
        return database.musicDao.searchTracksByNameOrArtist(query).map { it.toDomain() }
    }

    override suspend fun updateScannedMusicList(musicList: List<Track>) {
        return database.musicDao.updateAllInsertedTracks(musicList.map { it.toEntity() })
    }

    override suspend fun scan(
        minDurationSeconds: Long,
        minFileSizeInKb: Long
    ): List<Track> {
        return withContext(Dispatchers.IO) {
            localMusicScanner.scanMusic(
                minDurationMs = minDurationSeconds * 1000,
                minFileSizeInBytes = minFileSizeInKb * 1024
            ).map { it.toDomain() }
        }
    }

    override suspend fun deleteTrackByIds(vararg trackId: Long) {
        database.musicDao.deleteByIds(trackId.toList())
    }

    override fun getPlaylistsWithCountFlow(): Flow<List<PlaylistWithSongsCount>> {
        return database.musicDao.getAllPlaylistsWithCountFlow()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun createPlaylist(playlistName: String): Long {
        return database.musicDao.createPlaylist(
            playlistName = playlistName
        )
    }

    override suspend fun isPlaylistExists(playlistName: String): Boolean {
        return database.musicDao.isPlaylistExists(playlistName)
    }

}