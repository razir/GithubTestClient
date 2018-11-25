package com.anton.medium.di

import com.anton.medium.datasource.MediumAuthApiBuilder
import com.anton.medium.datasource.MediumContentApiBuilder
import com.anton.medium.datasource.OkHttpClientBuilder
import com.anton.medium.datasource.auth.*
import com.anton.medium.datasource.content.MediumContentApi
import com.anton.medium.datasource.content.TokenInterceptor
import com.anton.medium.datasource.user.MediumUserLocalRepository
import com.anton.medium.datasource.user.MediumUserLocalRepositoryImpl
import com.anton.medium.datasource.user.MediumUserRemoteRepository
import com.anton.medium.datasource.user.MediumUserRemoteRepositoryImpl
import com.anton.medium.domain.usecase.*
import com.anton.medium.presentation.login.web.LoginWebViewModel
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

    factory<LoginUrlComposerUseCase> { LoginUrlComposerUseCaseImpl() }

    factory<LoginCallbackHandlerUseCase> { LoginCallbackHandlerUseCaseImpl() }

    factory<CallAdapter.Factory> { CoroutineCallAdapterFactory() }

    factory<Converter.Factory> { GsonConverterFactory.create() }

    factory<TokenInterceptor> { TokenInterceptor(get()) }

    factory<OkHttpClient>(name = OKHTTP_AUTH) { OkHttpClientBuilder().build() }

    factory<OkHttpClient>(name = OKHTTP_CONTENT) { OkHttpClientBuilder().build(get<TokenInterceptor>()) }

    single<MediumAuthApi> { MediumAuthApiBuilder(get(OKHTTP_AUTH), get(), get()).build() }

    single<MediumContentApi> { MediumContentApiBuilder(get(OKHTTP_CONTENT), get(), get()).build() }

    factory<MediumAuthLocalRepository> { MediumAuthLocalRepositoryImpl(get()) }

    factory<MediumAuthRemoteRepository> { MediumAuthRemoteRepositoryImpl(get()) }

    factory<MediumUserLocalRepository> { MediumUserLocalRepositoryImpl(get()) }

    factory<MediumUserRemoteRepository> { MediumUserRemoteRepositoryImpl(get()) }

    factory<AuthorizeUseCase> { AuthorizeUseCaseImpl(get(), get(), get(), get()) }
}