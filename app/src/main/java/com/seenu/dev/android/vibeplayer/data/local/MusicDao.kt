package com.seenu.dev.android.vibeplayer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import com.seenu.dev.android.vibeplayer.data.model.PlaylistWithSongsCountEntity
import com.seenu.dev.android.vibeplayer.data.model.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query(
        "SELECT * FROM music_track_table"
    )
    fun getAllScannedTracksFlow(): Flow<List<MusicTrackEntity>>

    @Query(
        "SELECT * FROM music_track_table"
    )
    suspend fun getScannedTracks(): List<MusicTrackEntity>

    @Query(
        "SELECT * FROM music_track_table WHERE name LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'"
    )
    suspend fun searchTracksByNameOrArtist(query: String): List<MusicTrackEntity>

    @Query(
        "SELECT * FROM music_track_table LIMIT :count OFFSET :offset"
    )
    suspend fun getScannedTracks(offset: Int = 0, count: Int = 100): List<MusicTrackEntity>

    @Query(
        "SELECT * FROM music_track_table WHERE trackId = :id LIMIT 1"
    )
    suspend fun getTrackById(id: Long): MusicTrackEntity?

    @Transaction
    suspend fun updateAllInsertedTracks(tracks: List<MusicTrackEntity>) {
        insert(tracks)
        val ids = tracks.map { it.trackId }
        deleteIfNotPresent(ids)
    }

    @Upsert()
    suspend fun insert(music: List<MusicTrackEntity>)

    @Delete
    suspend fun delete(entity: MusicTrackEntity)

    @Query(
        "DELETE FROM music_track_table WHERE trackId IN (:ids)"
    )
    suspend fun deleteByIds(ids: List<Long>)

    @Query(
        "DELETE FROM music_track_table WHERE trackId NOT IN (:ids)"
    )
    suspend fun deleteIfNotPresent(ids: List<Long>)

    @Query("DELETE FROM music_track_table WHERE trackId IN (:ids)")
    suspend fun deleteAll(ids: List<Long>)

    @Transaction
    @Query("""
        SELECT 
            p.*,
            COUNT(pc.trackId) AS songCount
        FROM playlists p
        LEFT JOIN playlist_song_cross_ref pc
            ON p.playlistId = pc.playlistId
        GROUP BY p.playlistId
    """)
    fun getAllPlaylistsWithCountFlow(): Flow<List<PlaylistWithSongsCountEntity>>

    @Query("INSERT INTO playlists (name) VALUES (:playlistName)")
    suspend fun createPlaylist(playlistName: String): Long

    @Query("INSERT INTO playlist_song_cross_ref (playlistId, trackId) VALUES (:playlistId, :trackId)")
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long)

    @Query("SELECT COUNT(*) FROM playlists WHERE name = :playlistName")
    suspend fun getPlaylistCountByName(playlistName: String): Long

    suspend fun isPlaylistExists(playlistName: String): Boolean {
        val count = getPlaylistCountByName(playlistName)
        return count > 0
    }

}