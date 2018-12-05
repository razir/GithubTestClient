package com.anton.github.di

import android.content.Context
import com.anton.github.datasource.auth.*
import com.anton.github.datasource.content.GithubRestApi
import com.anton.github.datasource.content.notifications.*
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepositoryImpl
import com.anton.github.datasource.content.user.UserRemoteRepository
import com.anton.github.datasource.content.user.UserRemoteRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideAuthLocalRepository(context: Context): GithubAuthLocalRepository {
        return GithubAuthLocalRepositoryImpl(context)
    }

    @Provides
    fun provideAuthRemoteRepository(api: GithubAuthApi): GithubAuthRemoteRepository {
        return GithubAuthRemoteRepositoryImpl(api)
    }

    @Provides
    fun provideUserLocalRepository(context: Context): UserLocalRepository {
        return UserLocalRepositoryImpl(context)
    }

    @Provides
    fun provideUserRemoteRepository(api: GithubRestApi): UserRemoteRepository {
        return UserRemoteRepositoryImpl(api)
    }

    @Provides
    fun provideNotificationRemoteRepository(api: GithubRestApi): NotificationRemoteRepository {
        return NotificationRemoteRepositoryImpl(api)
    }

    @Provides
    fun provideNotificationLocalRepository(notificationsDao: NotificationsDao, repositoryDao: RepositoryDao):NotificationLocalRepository {
        return NotificationLocalRepositoryImpl(notificationsDao, repositoryDao)
    }
}