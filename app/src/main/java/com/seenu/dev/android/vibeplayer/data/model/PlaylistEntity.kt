package com.seenu.dev.android.vibeplayer.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "playlists")
data class PlaylistEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val name: String,
)

@Entity(
    tableName = "playlist_song_cross_ref",
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long
)

data class PlaylistWithTracksEntity(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
    )
    val tracks: List<MusicTrackEntity>
)