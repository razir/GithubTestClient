package com.anton.github.application

import android.app.Application
import com.anton.github.di.appModule
import org.koin.android.ext.android.startKoin

class GithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}