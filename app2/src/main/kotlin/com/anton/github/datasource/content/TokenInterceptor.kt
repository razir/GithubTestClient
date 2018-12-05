package com.anton.github.datasource.content

import com.anton.github.datasource.auth.GithubAuthLocalRepository
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTH_KEY = "Authorization"
private const val HEADER_AUTH_VALUE = "Bearer"

class TokenInterceptor(
    private val authLocalRepository: GithubAuthLocalRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authLocalRepository.token?.accessToken
        val request = chain.request().newBuilder().addHeader(HEADER_AUTH_KEY, "$HEADER_AUTH_VALUE $token").build()
        return chain.proceed(request)
    }
}