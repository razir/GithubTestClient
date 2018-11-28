package com.anton.github.datasource.content.publication

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.GithubRestApi

interface PublicationRemoteRepository {

    suspend fun getNotifications(page: Int): List<Notification>
}

class PublicationRemoteRepositoryImpl(private val api: GithubRestApi) : PublicationRemoteRepository {

    override suspend fun getNotifications(page: Int): List<Notification> {
        return api.getUserNotifications(page).await()
    }

}