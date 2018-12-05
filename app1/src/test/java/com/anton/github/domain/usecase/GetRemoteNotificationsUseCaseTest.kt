package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetRemoteNotificationsUseCaseTest {

    lateinit var usecase: GetRemoteNotificationsUseCase
    val notificationRemoteRepository: NotificationRemoteRepository = mock()
    val notificationLocalRepository: NotificationLocalRepository = mock()

    @Before
    fun setup() {
        usecase = GetRemoteNotificationsUseCaseImpl(notificationRemoteRepository, notificationLocalRepository)
    }

    @Test
    fun remoteNotiFailed() {
        whenever(runBlocking { notificationRemoteRepository.getNotifications(any()) }).thenAnswer {
            IOException()
        }
        val result = runBlocking {
            usecase.run(0)
        }
        assertTrue(result is ErrorUseCase)
    }

    @Test
    fun remoteNotiFirstPage() {
        val testNotiList = listOf(Notification())
        whenever(runBlocking { notificationRemoteRepository.getNotifications(any()) }).doReturn(testNotiList)
        val result = runBlocking {
            usecase.run(0)
        }
        assertTrue(result is SuccessUseCase)
        assertEquals(testNotiList, (result as SuccessUseCase).result)
        verify(notificationLocalRepository).saveNotifications(testNotiList)
    }

    @Test
    fun remoteNotiSecondPage() {
        val testNotiList = listOf(Notification())
        whenever(runBlocking { notificationRemoteRepository.getNotifications(any()) }).doReturn(testNotiList)
        val result = runBlocking {
            usecase.run(1)
        }
        assertTrue(result is SuccessUseCase)
        assertEquals(testNotiList, (result as SuccessUseCase).result)
        verify(notificationLocalRepository, never()).saveNotifications(testNotiList)
    }
}