package com.anton.github2.di

import com.anton.github2.datasource.auth.GithubAuthLocalRepository
import com.anton.github2.datasource.auth.GithubAuthRemoteRepository
import com.anton.github2.datasource.content.notifications.NotificationLocalRepository
import com.anton.github2.datasource.content.notifications.NotificationRemoteRepository
import com.anton.github2.datasource.content.user.UserLocalRepository
import com.anton.github2.datasource.content.user.UserRemoteRepository
import com.anton.github2.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideLoginUrlComposerUseCase(): LoginUrlComposerUseCase = LoginUrlComposerUseCaseImpl()

    @Provides
    fun provideLoginCallbackHandlerUseCase(): LoginCallbackHandlerUseCase = LoginCallbackHandlerUseCaseImpl()

    @Provides
    fun provideAuthorizeUseCase(
        tokenRemoteRepository: GithubAuthRemoteRepository,
        tokenLocalRepository: GithubAuthLocalRepository,
        userLocalRepository: UserLocalRepository,
        userRemoteRemoteRepository: UserRemoteRepository
    ): AuthorizeUseCase {

        return AuthorizeUseCaseImpl(
            tokenRemoteRepository,
            tokenLocalRepository,
            userLocalRepository,
            userRemoteRemoteRepository
        )
    }

    @Provides
    fun provideGetRemoteNotificationsUseCase(
        notificationRemoteRepository: NotificationRemoteRepository,
        notificationsLocalRepository: NotificationLocalRepository
    ): GetRemoteNotificationsUseCase {
        return GetRemoteNotificationsUseCaseImpl(notificationRemoteRepository, notificationsLocalRepository)
    }

    @Provides
    fun provideGetRemoteUserProfileUseCase(
        userRemoteRepository: UserRemoteRepository,
        userLocalRepository: UserLocalRepository
    ): GetRemoteUserProfileUseCase {
        return GetRemoteUserProfileUseCaseImpl(userRemoteRepository, userLocalRepository)
    }

    @Provides
    fun provideGetLocalNotificationsUseCase(notificationLocalRepository: NotificationLocalRepository): GetLocalNotificationsUseCase {
        return GetLocalNotificationsUseCaseImpl(notificationLocalRepository)
    }

    @Provides
    fun provideGetDetailsUrlUseCase(notificationRemoteRepository: NotificationRemoteRepository): GetDetailsUrlUseCase {
        return GetDetailsUseUrlCaseImpl(notificationRemoteRepository)
    }

    @Provides
    fun provideLogoutUseCase(
        userLocalRepository: UserLocalRepository,
        authLocalRepository: GithubAuthLocalRepository,
        notificationLocalRepository: NotificationLocalRepository
    ): LogoutUseCase {
        return LogoutUseCaseImpl(userLocalRepository, authLocalRepository, notificationLocalRepository)
    }
}