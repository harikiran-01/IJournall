package com.hk.ijournal

import android.app.Application
import com.github.anrwatchdog.ANRWatchDog
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IJournallApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
        ANRWatchDog().start()
    }
}