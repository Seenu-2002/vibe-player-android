package com.seenu.dev.android.vibeplayer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query(
        "SELECT * FROM music_track_table"
    )
    fun getAllScannedTracksFlow(): Flow<List<MusicTrackEntity>>

    @Query(
        "SELECT * FROM music_track_table WHERE id = :id LIMIT 1"
    )
    suspend  fun getTrackById(id: Long): MusicTrackEntity?

    @Transaction
    suspend fun updateAllInsertedTracks(tracks: List<MusicTrackEntity>) {
        insert(tracks)
        val ids = tracks.map { it.id }
        deleteIfNotPresent(ids)
    }

    @Upsert()
    suspend fun insert(music: List<MusicTrackEntity>)

    @Delete
    suspend fun delete(id: MusicTrackEntity)

    @Query(
        "DELETE FROM music_track_table WHERE id NOT IN (:ids)"
    )
    suspend fun deleteIfNotPresent(ids: List<Long>)

    @Query("DELETE FROM music_track_table WHERE id IN (:ids)")
    suspend fun deleteAll(ids: List<Long>)

}