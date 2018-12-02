package com.anton.github.datasource

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.anton.github.data.api.response.TokenResponse
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.auth.GithubAuthLocalRepositoryImpl
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubAuthLocalRepositoryTest {

    lateinit var repository: GithubAuthLocalRepository

    @Before
    fun setup() {
        repository = GithubAuthLocalRepositoryImpl(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun setToken() {
        val testToken = TokenResponse().apply {
            tokenType = "token_type_test"
            accessToken = "access_token_test"
            scope = "scope_test"
        }
        repository.token = testToken
        val savedToken = repository.token
        assertTrue(tokenEquals(testToken, savedToken))
    }

    @Test
    fun clearToken() {
        val testToken = TokenResponse().apply {
            accessToken = "token"
        }
        repository.token = testToken
        repository.token = null
        val savedToken = repository.token
        assertTrue(savedToken == null)
    }

    private fun tokenEquals(token1: TokenResponse?, token2: TokenResponse?): Boolean {
        if (token1?.accessToken != token2?.accessToken) return false
        if (token1?.scope != token2?.scope) return false
        if (token1?.tokenType != token2?.tokenType) return false
        return true
    }
}