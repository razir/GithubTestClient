package com.anton.github.domain.usecase

import com.anton.github.data.api.response.TokenResponse
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.auth.GithubAuthRemoteRepository
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AuthorizeUseCaseTest {

    lateinit var useCase: AuthorizeUseCase
    val tokenRemoteRepository: GithubAuthRemoteRepository = mock()
    val tokenLocalRepository: GithubAuthLocalRepository = mock()
    val userLocalRepository: UserLocalRepository = mock()
    val userRemoteRepository: UserRemoteRepository = mock()

    val testCode = "code"

    @Before
    fun setup() {
        useCase = AuthorizeUseCaseImpl(
            tokenRemoteRepository,
            tokenLocalRepository,
            userLocalRepository,
            userRemoteRepository
        )
    }

    @Test
    fun tokenRemoteError() {
        whenever(runBlocking { tokenRemoteRepository.getTokenByCode(any()) }).thenAnswer {
            throw IOException()
        }
        val result = runBlocking {
            return@runBlocking useCase.run(testCode)
        }
        assertTrue(result is ErrorUseCase)
        verify(tokenLocalRepository).token = null
        verify(userLocalRepository).userProfile = null
    }

    @Test
    fun userProfileRemoteError() {
        val testToken = TokenResponse()
        whenever(runBlocking { tokenRemoteRepository.getTokenByCode(any()) }).doReturn(testToken)
        whenever(runBlocking { userRemoteRepository.getProfile() }).thenAnswer {
            RuntimeException()
        }
        val result = runBlocking {
            return@runBlocking useCase.run(testCode)
        }
        assertTrue(result is ErrorUseCase)
        verify(tokenLocalRepository).token = testToken
        verify(tokenLocalRepository).token = null
        verify(userLocalRepository).userProfile = null
    }

    @Test
    fun success() {
        val testToken = TokenResponse()
        val testProfile = UserProfile()
        whenever(runBlocking { tokenRemoteRepository.getTokenByCode(any()) }).doReturn(testToken)
        whenever(runBlocking { userRemoteRepository.getProfile() }).doReturn(testProfile)

        val result = runBlocking {
            return@runBlocking useCase.run(testCode)
        }
        assertTrue(result is CompletedUseCase)
        verify(tokenLocalRepository).token = testToken
        verify(userLocalRepository).userProfile = testProfile
    }
}