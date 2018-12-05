package com.anton.github.di

import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.auth.GithubAuthLocalRepositoryImpl
import com.anton.github.datasource.auth.GithubAuthRemoteRepository
import com.anton.github.datasource.auth.GithubAuthRemoteRepositoryImpl
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepositoryImpl
import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import com.anton.github.datasource.content.notifications.NotificationRemoteRepositoryImpl
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepositoryImpl
import com.anton.github.datasource.content.user.UserRemoteRepository
import com.anton.github.datasource.content.user.UserRemoteRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {

    factory<GithubAuthLocalRepository> { GithubAuthLocalRepositoryImpl(get()) }

    factory<GithubAuthRemoteRepository> { GithubAuthRemoteRepositoryImpl(get()) }

    factory<UserLocalRepository> { UserLocalRepositoryImpl(get()) }

    factory<UserRemoteRepository> { UserRemoteRepositoryImpl(get()) }

    factory<NotificationRemoteRepository> { NotificationRemoteRepositoryImpl(get()) }

    factory<NotificationLocalRepository> { NotificationLocalRepositoryImpl(get(), get()) }
}