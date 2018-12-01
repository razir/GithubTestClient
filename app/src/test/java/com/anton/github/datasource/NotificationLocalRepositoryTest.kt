package com.anton.github.datasource

import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.Repository
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepositoryImpl
import com.anton.github.datasource.content.notifications.NotificationsDao
import com.anton.github.datasource.content.notifications.RepositoryDao
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class NotificationLocalRepositoryTest {

    lateinit var repository: NotificationLocalRepository
    val notificationDao: NotificationsDao = mock()
    val repositoryDao: RepositoryDao = mock()

    @Before
    fun setup() {
        repository = NotificationLocalRepositoryImpl(notificationDao, repositoryDao)
    }

    @Test
    fun getNotifications() {
        whenever(notificationDao.getAll()).doReturn(testNotificationsGet())
        whenever(repositoryDao.getForNotificationId(any())).thenAnswer {
            val idArg: String = it.getArgument(0)
            Repository().apply {
                id = idArg
            }
        }
        val notiList = repository.getNotifications()
        assertEquals("1", notiList[0].repository?.id)
        assertEquals("2", notiList[1].repository?.id)
    }

    @Test
    fun saveNotifications() {
        val noti = testNotificationsSave()
        val captor = argumentCaptor<List<Notification>>()

        repository.saveNotifications(noti)
        verify(notificationDao).deleteAll()
        verify(repositoryDao).deleteAll()
        verify(notificationDao).save(captor.capture())

        val notiToSave = captor.firstValue
        assertEquals("1", notiToSave[0].repositoryId)
        assertEquals("2", notiToSave[1].repositoryId)
    }

    @Test
    fun deleteAll() {
        repository.deleteAll()
        verify(notificationDao).deleteAll()
        verify(repositoryDao).deleteAll()
    }

    private fun testNotificationsGet(): List<Notification> {
        val notification1 = Notification().apply {
            repositoryId = "1"
        }
        val notification2 = Notification().apply {
            repositoryId = "2"
        }
        return listOf(notification1, notification2)
    }

    private fun testNotificationsSave(): List<Notification> {
        val notification1 = Notification().apply {
            repository = Repository().apply {
                id = "1"
            }
        }
        val notification2 = Notification().apply {
            repository = Repository().apply {
                id = "2"
            }
        }
        return listOf(notification1, notification2)
    }
}