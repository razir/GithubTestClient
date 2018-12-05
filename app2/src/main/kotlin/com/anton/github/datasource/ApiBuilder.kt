package com.anton.github.datasource

import com.anton.github.BuildConfig
import com.anton.github.constants.BASE_GITHUB_API_URL
import com.anton.github.constants.GITHUB_LOGIN_BASE_URL
import com.anton.github.datasource.auth.GithubAuthApi
import com.anton.github.datasource.content.GithubRestApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class OkHttpClientBuilder() {
    fun build(vararg interceptors: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors.forEach {
                addInterceptor(it)
            }
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }
        }.build()
    }
}

class GithubAuthApiBuilder(
    private val okHttpClient: OkHttpClient,
    private val callAdapterFactory: CallAdapter.Factory,
    private val converterFactory: Converter.Factory
) {

    fun build(): GithubAuthApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(GITHUB_LOGIN_BASE_URL)
            .build()
            .create(GithubAuthApi::class.java)
    }
}

class GithubRestApiBuilder(
    private val okHttpClient: OkHttpClient,
    private val callAdapterFactory: CallAdapter.Factory,
    private val converterFactory: Converter.Factory
) {

    fun build(): GithubRestApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_GITHUB_API_URL)
            .build()
            .create(GithubRestApi::class.java)
    }
}
