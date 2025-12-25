package com.seenu.dev.android.vibeplayer.di

import android.content.Context
import androidx.room.Room
import com.seenu.dev.android.vibeplayer.data.local.VibePlayerDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.seenu.dev.android.vibeplayer")
class AppModule {

    @Single
    fun providesVibePlayerDatabase(context: Context): VibePlayerDatabase {
        return Room.databaseBuilder(
            context,
            VibePlayerDatabase::class.java,
            "vibe_player_database.db"
        ).build()
    }

}