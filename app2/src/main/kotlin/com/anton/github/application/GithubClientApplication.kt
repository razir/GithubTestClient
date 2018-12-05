package com.anton.github.application

import com.anton.github.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GithubClientApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build().apply {
                inject(this@GithubClientApplication)
            }
    }
}