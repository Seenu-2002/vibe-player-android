package com.seenu.dev.android.vibeplayer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import com.seenu.dev.android.vibeplayer.data.model.PlaylistEntity
import com.seenu.dev.android.vibeplayer.data.model.PlaylistTrackCrossRef
import org.koin.core.annotation.Single

@Database(
    entities = [MusicTrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class],
    version = 1,
)
abstract class VibePlayerDatabase constructor() : RoomDatabase() {

    abstract val musicDao: MusicDao

}