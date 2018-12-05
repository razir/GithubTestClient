package com.anton.github2.di

import android.app.Application
import com.anton.github2.application.GithubClientApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        SplashModule::class,
        UseCaseModule::class,
        ApiModule::class,
        LoginModule::class
    ]
)
interface AppComponent : AndroidInjector<GithubClientApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}