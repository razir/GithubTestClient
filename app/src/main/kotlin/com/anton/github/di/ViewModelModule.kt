package com.anton.github.di

import com.anton.github.presentation.login.web.LoginWebViewModel
import com.anton.github.presentation.profile.NotificationsViewModel
import com.anton.github.presentation.profile.ProfileViewModel
import com.anton.github.presentation.splash.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { LoginWebViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { NotificationsViewModel(get(), get(), get()) }
}