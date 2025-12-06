package com.seenu.dev.android.vibeplayer

import android.app.Application
import timber.log.Timber

class VibePlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}