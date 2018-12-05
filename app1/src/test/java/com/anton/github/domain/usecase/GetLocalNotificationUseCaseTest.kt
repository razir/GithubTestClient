package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocalNotificationUseCaseTest {

    lateinit var useCase: GetLocalNotificationsUseCase
    val notificationLocalRepository: NotificationLocalRepository = mock()

    @Before
    fun setup() {
        useCase = GetLocalNotificationsUseCaseImpl(notificationLocalRepository)
    }

    @Test
    fun getNotiFromCache() {
        val notificationsList = listOf(Notification())
        whenever(notificationLocalRepository.getNotifications()).doReturn(notificationsList)

        val result = runBlocking {
            useCase.run()
        }
        assertEquals(notificationsList, (result as SuccessUseCase<List<Notification>>).result)
    }
}