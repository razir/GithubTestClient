package com.anton.github.domain.usecase

import com.anton.github.data.api.response.DetailsResponse
import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetDetailsUseCaseTest {

    lateinit var usecase: GetDetailsUrlUseCase
    val notificationsRemoteRepository: NotificationRemoteRepository = mock()
    val testUrl = "http://test"

    @Before
    fun setup() {
        usecase = GetDetailsUseUrlCaseImpl(notificationsRemoteRepository)
    }

    @Test
    fun detailsFailed() {
        whenever(runBlocking { notificationsRemoteRepository.getDetails(any()) }).thenAnswer {
            IOException()
        }
        val result = runBlocking {
            usecase.run(testUrl)
        }
        assertTrue(result is ErrorUseCase)
    }

    @Test
    fun detailsUrlNull() {
        whenever(runBlocking { notificationsRemoteRepository.getDetails(any()) }).doReturn(DetailsResponse())
        val result = runBlocking {
            usecase.run(testUrl)
        }
        assertTrue(result is ErrorUseCase)
    }

    @Test
    fun detailsSuccess() {
        whenever(runBlocking { notificationsRemoteRepository.getDetails(any()) }).doReturn(DetailsResponse().apply {
            htmlUrl = testUrl
        })
        val result = runBlocking {
            usecase.run(testUrl)
        }
        assertTrue(result is SuccessUseCase)
        assertEquals(testUrl, (result as SuccessUseCase).result)
    }
}