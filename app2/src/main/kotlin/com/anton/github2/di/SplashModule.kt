package com.anton.github2.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.anton.github2.datasource.auth.GithubAuthLocalRepository
import com.anton.github2.presentation.splash.SplashActivity
import com.anton.github2.presentation.splash.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = arrayOf(
        SplashModule.ProvideViewModel::class
    )
)
abstract class SplashModule {

    @ContributesAndroidInjector(
        modules = [
            InjectViewModel::class
        ]
    )
    abstract fun bind(): SplashActivity

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(SplashViewModel::class)
        fun provideSplashViewModel(authRepository: GithubAuthLocalRepository): ViewModel =
            SplashViewModel(authRepository)
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideSplashViewModel(
            factory: ViewModelProvider.Factory,
            target: SplashActivity
        ): SplashViewModel =
            ViewModelProviders.of(target, factory).get(SplashViewModel::class.java)
    }
}