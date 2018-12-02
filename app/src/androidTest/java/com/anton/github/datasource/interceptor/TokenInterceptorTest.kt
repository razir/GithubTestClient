package com.anton.github.datasource.interceptor

import android.support.test.runner.AndroidJUnit4
import com.anton.github.data.api.response.TokenResponse
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.content.TokenInterceptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TokenInterceptorTest {

    @Test
    fun testTokenInterceptor() {
        val authLocalRepository: GithubAuthLocalRepository = mock()
        val testAccessToken = "123456123456"

        whenever(authLocalRepository.token).doReturn(TokenResponse().apply {
            accessToken = testAccessToken
        })

        val mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse())
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(TokenInterceptor(authLocalRepository)).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val request = mockWebServer.takeRequest()
        assertEquals("Bearer $testAccessToken", request.getHeader("Authorization"))
    }
}