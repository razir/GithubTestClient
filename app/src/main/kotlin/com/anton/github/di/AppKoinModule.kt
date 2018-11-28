package com.anton.github.di

import com.anton.github.datasource.GithubAuthApiBuilder
import com.anton.github.datasource.GithubRestApiBuilder
import com.anton.github.datasource.OkHttpClientBuilder
import com.anton.github.datasource.auth.*
import com.anton.github.datasource.content.GithubRestApi
import com.anton.github.datasource.content.TokenInterceptor
import com.anton.github.datasource.content.publication.PublicationRemoteRepository
import com.anton.github.datasource.content.publication.PublicationRemoteRepositoryImpl
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepositoryImpl
import com.anton.github.datasource.content.user.UserRemoteRepository
import com.anton.github.datasource.content.user.UserRemoteRepositoryImpl
import com.anton.github.domain.usecase.*
import com.anton.github.presentation.login.web.LoginWebViewModel
import com.anton.github.presentation.profile.ProfileViewModel
import com.anton.github.presentation.splash.SplashViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.ext.koin.viewModel

import org.koin.dsl.module.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

private const val OKHTTP_AUTH = "okhttp_auth"
private const val OKHTTP_CONTENT = "okhttp_content"

val appModule = module {

    viewModel { LoginWebViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }


    factory<LoginUrlComposerUseCase> { LoginUrlComposerUseCaseImpl() }

    factory<LoginCallbackHandlerUseCase> { LoginCallbackHandlerUseCaseImpl() }

    factory<CallAdapter.Factory> { CoroutineCallAdapterFactory() }

    factory<Converter.Factory> { GsonConverterFactory.create() }

    factory<TokenInterceptor> { TokenInterceptor(get()) }

    factory<OkHttpClient>(name = OKHTTP_AUTH) { OkHttpClientBuilder().build() }

    factory<OkHttpClient>(name = OKHTTP_CONTENT) { OkHttpClientBuilder().build(get<TokenInterceptor>()) }

    single<GithubAuthApi> { GithubAuthApiBuilder(get(OKHTTP_AUTH), get(), get()).build() }

    single<GithubRestApi> { GithubRestApiBuilder(get(OKHTTP_CONTENT), get(), get()).build() }

    factory<GithubAuthLocalRepository> { GithubAuthLocalRepositoryImpl(get()) }

    factory<GithubAuthRemoteRepository> { GithubAuthRemoteRepositoryImpl(get()) }

    factory<UserLocalRepository> { UserLocalRepositoryImpl(get()) }

    factory<UserRemoteRepository> { UserRemoteRepositoryImpl(get()) }

    factory<PublicationRemoteRepository> { PublicationRemoteRepositoryImpl(get()) }

    factory<AuthorizeUseCase> { AuthorizeUseCaseImpl(get(), get(), get(), get()) }

    factory<GetNotificationsUseCase> { GetNotificationsUseCaseImpl(get()) }

    factory<GetRemoteUserProfileUseCase> { GetRemoteUserProfileUseCaseImpl(get(), get()) }
}