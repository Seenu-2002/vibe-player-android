package com.seenu.dev.android.vibeplayer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import org.koin.core.annotation.Single

@Database(
    entities = [MusicTrackEntity::class],
    version = 1,
)
abstract class VibePlayerDatabase constructor() : RoomDatabase() {

    abstract val musicDao: MusicDao

}