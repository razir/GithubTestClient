package com.anton.github.datasource.content.notifications

import com.anton.github.data.entity.Notification


interface NotificationLocalRepository {
    fun getNotifications(): List<Notification>

    fun saveNotifications(data: List<Notification>)

    fun deleteAll()
}

class NotificationLocalRepositoryImpl(
    private val notificationsDao: NotificationsDao,
    private val repositoryDao: RepositoryDao
) : NotificationLocalRepository {

    override fun getNotifications(): List<Notification> {
        val notifications = notificationsDao.getAll()
        notifications.forEach {
            it.id?.let { id ->
                val repository = repositoryDao.getForNotificationId(id)
                it.repository = repository
            }
        }
        return notifications
    }

    override fun saveNotifications(data: List<Notification>) {
        notificationsDao.deleteAll()
        notificationsDao.save(data)
        data.forEach { notificaion ->
            notificaion.repository?.let {
                it.notificationId = notificaion.id
                repositoryDao.save(it)
            }
        }
    }

    override fun deleteAll() {
        notificationsDao.deleteAll()
    }

}