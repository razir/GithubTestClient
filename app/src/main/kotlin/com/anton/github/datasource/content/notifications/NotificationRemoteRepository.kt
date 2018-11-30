package com.anton.github.datasource.content.notifications

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.GithubRestApi

interface NotificationRemoteRepository {

    suspend fun getNotifications(page: Int): List<Notification>
}

class NotificationRemoteRepositoryImpl(private val api: GithubRestApi) : NotificationRemoteRepository {

    override suspend fun getNotifications(page: Int): List<Notification> {
        return api.getUserNotifications(page).await()
    }

}