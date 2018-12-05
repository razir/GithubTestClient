package com.anton.github.datasource

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.Repository
import com.anton.github.data.entity.Subject
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepositoryImpl
import com.anton.github.readFromFile
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotificationLocalRepositoryAndroidTest {

    lateinit var repository: NotificationLocalRepository
    lateinit var appDatabase: AppDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        appDatabase = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        val notificationsDao = appDatabase.notificationDao()
        val repositoryDao = appDatabase.repositoryDao()
        repository = NotificationLocalRepositoryImpl(notificationsDao, repositoryDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun saveNotifications() {
        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        repository.saveNotifications(testNoti)
        //Try to save again
        repository.saveNotifications(testNoti)

        val cachedNoti = repository.getNotifications()
        assertTrue(compareNotifications(testNoti, cachedNoti))
    }

    @Test
    fun deleteAll() {
        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        repository.saveNotifications(testNoti)

        repository.deleteAll()
        val noti = repository.getNotifications()
        assertTrue(noti.isEmpty())
    }

    fun compareNotifications(notifcations1: List<Notification>, notifcations2: List<Notification>): Boolean {
        if (notifcations1.size != notifcations2.size) return false
        notifcations1.forEachIndexed { index, notifcation1 ->
            if (!compareNotifications(notifcation1, notifcations2[index])) return false
        }
        return true
    }

    fun compareNotifications(notification1: Notification?, notification2: Notification?): Boolean {
        if (notification1?.id != notification2?.id) return false
        if (notification1?.reason != notification2?.reason) return false
        if (notification1?.unread != notification2?.unread) return false
        if (notification1?.url != notification2?.url) return false
        if (notification1?.updatedAt != notification2?.updatedAt) return false
        if (!compareRepositores(notification1?.repository, notification2?.repository)) return false
        if (!compareSubjects(notification1?.subject, notification2?.subject)) return false
        return true
    }

    fun compareSubjects(subject1: Subject?, subject2: Subject?): Boolean {
        if (subject1?.title != subject2?.title) return false
        if (subject1?.url != subject2?.url) return false
        if (subject1?.type != subject2?.type) return false
        return true
    }

    fun compareRepositores(repository1: Repository?, repository2: Repository?): Boolean {
        if (repository1?.id != repository2?.id) return false
        if (repository1?.name != repository2?.name) return false
        if (repository1?.fullName != repository2?.fullName) return false
        return true
    }

}