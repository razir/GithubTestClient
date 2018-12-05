package com.anton.github.di

import com.anton.github.datasource.GithubAuthApiBuilder
import com.anton.github.datasource.GithubRestApiBuilder
import com.anton.github.datasource.OkHttpClientBuilder
import com.anton.github.datasource.auth.GithubAuthApi
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.content.GithubRestApi
import com.anton.github.datasource.content.TokenInterceptor
import com.anton.github.utils.NetworkStatusProviderImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val OKHTTP_AUTH = "okhttp_auth"
private const val OKHTTP_CONTENT = "okhttp_content"

@Module
class ApiModule {

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideConverterAdapterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideTokenInterceptor(githubAuthLocalRepository: GithubAuthLocalRepository): TokenInterceptor {
        return TokenInterceptor(githubAuthLocalRepository)
    }

    @Provides
    @Named(OKHTTP_AUTH)
    fun provideOkHttpAuth(): OkHttpClient {
        return OkHttpClientBuilder().build()
    }

    @Provides
    @Named(OKHTTP_CONTENT)
    fun provideOkHttpContent(tokenInterceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClientBuilder().build(tokenInterceptor)
    }

    @Singleton
    @Provides
    fun providesAuthApi(
        @Named(OKHTTP_AUTH) okHttpClient: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ): GithubAuthApi {
        return GithubAuthApiBuilder(okHttpClient, callAdapter, converter).build()
    }

    @Singleton
    @Provides
    fun provideContentApi(
        @Named(OKHTTP_CONTENT) okHttpClient: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ): GithubRestApi {
        return GithubRestApiBuilder(okHttpClient, callAdapter, converter).build()
    }
}