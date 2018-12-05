package com.anton.github.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.anton.github.utils.NetworkStatusProvider
import com.anton.github.utils.NetworkStatusProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideNetworkStatusProvider(context: Context): NetworkStatusProvider {
        return NetworkStatusProviderImpl(context)
    }

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return AppViewModelFactory(providers)
    }
}