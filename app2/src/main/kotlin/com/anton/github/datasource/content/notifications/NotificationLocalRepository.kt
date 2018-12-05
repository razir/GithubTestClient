package com.anton.github.datasource.content.notifications

import com.anton.github.data.entity.Notification
import io.reactivex.Completable
import io.reactivex.Single


interface NotificationLocalRepository {
    fun getNotifications(): Single<List<Notification>>

    fun saveNotifications(data: List<Notification>): Completable

    fun deleteAll(): Completable
}

class NotificationLocalRepositoryImpl(
    private val notificationsDao: NotificationsDao,
    private val repositoryDao: RepositoryDao
) : NotificationLocalRepository {

    override fun getNotifications(): Single<List<Notification>> {
        return Single.fromCallable {
            val notifications = notificationsDao.getAll()
            notifications.forEach {
                val repository = repositoryDao.getForNotificationId(it.repositoryId)
                it.repository = repository
            }
            notifications
        }
    }

    override fun saveNotifications(data: List<Notification>): Completable {
        return Completable.fromCallable {
            notificationsDao.deleteAll()
            repositoryDao.deleteAll()
            data.forEach {
                it.repositoryId = it.repository?.id ?: ""
                it.repository?.let {
                    repositoryDao.save(it)
                }
            }
            notificationsDao.save(data)
        }
    }

    override fun deleteAll(): Completable {
        return Completable.fromCallable {
            notificationsDao.deleteAll()
            repositoryDao.deleteAll()
        }
    }

}