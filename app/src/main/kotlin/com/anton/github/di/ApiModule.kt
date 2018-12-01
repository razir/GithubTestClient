package com.anton.github.di

import com.anton.github.datasource.GithubAuthApiBuilder
import com.anton.github.datasource.GithubRestApiBuilder
import com.anton.github.datasource.OkHttpClientBuilder
import com.anton.github.datasource.auth.GithubAuthApi
import com.anton.github.datasource.content.GithubRestApi
import com.anton.github.datasource.content.TokenInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

private const val OKHTTP_AUTH = "okhttp_auth"
private const val OKHTTP_CONTENT = "okhttp_content"

val apiModule = module {
    factory<CallAdapter.Factory> { CoroutineCallAdapterFactory() }

    factory<Converter.Factory> { GsonConverterFactory.create() }

    factory<TokenInterceptor> { TokenInterceptor(get()) }

    factory<OkHttpClient>(name = OKHTTP_AUTH) { OkHttpClientBuilder().build() }

    factory<OkHttpClient>(name = OKHTTP_CONTENT) { OkHttpClientBuilder().build(get<TokenInterceptor>()) }

    single<GithubAuthApi> { GithubAuthApiBuilder(get(OKHTTP_AUTH), get(), get()).build() }

    single<GithubRestApi> { GithubRestApiBuilder(get(OKHTTP_CONTENT), get(), get()).build() }
}