package com.seenu.dev.android.vibeplayer.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_track_table")
data class MusicTrackEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val musicId: Long,
    @ColumnInfo(
        index = true
    )
    val name: String,
    @ColumnInfo(
        index = true
    )
    val path: String,
    val artist: String?,
    val albumId: Long,
    val album: String?,
    val size: Long,
    val duration: Long
)