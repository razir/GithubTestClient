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
            val repository = repositoryDao.getForNotificationId(it.repositoryId)
            it.repository = repository
        }
        return notifications
    }

    override fun saveNotifications(data: List<Notification>) {
        notificationsDao.deleteAll()
        data.forEach {
            it.repositoryId = it.repository?.id ?: ""
            it.repository?.let {
                repositoryDao.save(it)
            }
        }
        notificationsDao.save(data)
    }

    override fun deleteAll() {
        notificationsDao.deleteAll()
        repositoryDao.deleteAll()
    }

}