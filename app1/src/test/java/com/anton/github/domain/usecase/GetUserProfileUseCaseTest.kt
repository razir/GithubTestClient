package com.anton.github.domain.usecase

import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetUserProfileUseCaseTest {

    lateinit var usecase: GetRemoteUserProfileUseCase
    val userRemoteRepository: UserRemoteRepository = mock()
    val userLocalRepository: UserLocalRepository = mock()

    @Before
    fun setup() {
        usecase = GetRemoteUserProfileUseCaseImpl(userRemoteRepository, userLocalRepository)
    }

    @Test
    fun remoteFailed() {
        whenever(runBlocking { userRemoteRepository.getProfile() }).thenAnswer {
            IOException()
        }
        val result = runBlocking {
            usecase.run()
        }
        assertTrue(result is ErrorUseCase)
    }

    @Test
    fun getProfileSuccess() {
        val testProfile = UserProfile()
        whenever(runBlocking { userRemoteRepository.getProfile() }).doReturn(testProfile)

        val result = runBlocking {
            usecase.run()
        }
        assertEquals(testProfile, (result as SuccessUseCase<UserProfile>).result)
        verify(userLocalRepository).userProfile = testProfile
    }
}