package com.anton.medium.datasource

import com.anton.medium.BuildConfig
import com.anton.medium.constants.BASE_MEDIUM_API_URL
import com.anton.medium.datasource.auth.MediumAuthApi
import com.anton.medium.datasource.content.MediumContentApi
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

class MediumAuthApiBuilder(
    private val okHttpClient: OkHttpClient,
    private val callAdapterFactory: CallAdapter.Factory,
    private val converterFactory: Converter.Factory
) {

    fun build(): MediumAuthApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_MEDIUM_API_URL)
            .build()
            .create(MediumAuthApi::class.java)
    }
}

class MediumContentApiBuilder(
    private val okHttpClient: OkHttpClient,
    private val callAdapterFactory: CallAdapter.Factory,
    private val converterFactory: Converter.Factory

) {

    fun build(): MediumContentApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_MEDIUM_API_URL)
            .build()
            .create(MediumContentApi::class.java)
    }
}
