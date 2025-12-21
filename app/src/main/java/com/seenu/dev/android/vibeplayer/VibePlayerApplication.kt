package com.seenu.dev.android.vibeplayer

import android.app.Application
import com.seenu.dev.android.vibeplayer.di.VibePlayerDiApplication
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.startKoin
import timber.log.Timber

class VibePlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        VibePlayerDiApplication.startKoin {
            androidContext(this@VibePlayerApplication)
        }
    }

}