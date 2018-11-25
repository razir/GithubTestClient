package com.anton.medium.application

import android.app.Application
import com.anton.medium.di.appModule
import org.koin.android.ext.android.startKoin

class MediumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}