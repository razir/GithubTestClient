package com.anton.github2.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.anton.github2.datasource.auth.GithubAuthLocalRepository
import com.anton.github2.datasource.content.user.UserLocalRepository
import com.anton.github2.domain.usecase.*
import com.anton.github2.presentation.profile.NotificationsViewModel
import com.anton.github2.presentation.profile.ProfileActivity
import com.anton.github2.presentation.profile.ProfileViewModel
import com.anton.github2.presentation.splash.SplashActivity
import com.anton.github2.presentation.splash.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = arrayOf(
        ProfileModule.ProvideViewModel::class
    )
)
abstract class ProfileModule {

    @ContributesAndroidInjector(
        modules = [
            InjectViewModel::class
        ]
    )
    abstract fun bind(): ProfileActivity

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(ProfileViewModel::class)
        fun provideProfileViewModel(
            userProfileLocalRepository: UserLocalRepository,
            getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
            logoutUseCase: LogoutUseCase
        ): ViewModel =
            ProfileViewModel(userProfileLocalRepository, getRemoteUserProfileUseCase, logoutUseCase)

        @Provides
        @IntoMap
        @ViewModelKey(NotificationsViewModel::class)
        fun provideNotificationsViewModel(
            getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase,
            getLocalNotificationsUseCase: GetLocalNotificationsUseCase,
            getDetailsUrlUseCase: GetDetailsUrlUseCase
        ): ViewModel =
            NotificationsViewModel(getRemoteNotificationsUseCase, getLocalNotificationsUseCase, getDetailsUrlUseCase)
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideProfileViewModel(
            factory: ViewModelProvider.Factory,
            target: ProfileActivity
        ): ProfileViewModel =
            ViewModelProviders.of(target, factory).get(ProfileViewModel::class.java)

        @Provides
        fun provideNotificationsViewModel(
            factory: ViewModelProvider.Factory,
            target: ProfileActivity
        ): NotificationsViewModel =
            ViewModelProviders.of(target, factory).get(NotificationsViewModel::class.java)
    }
}