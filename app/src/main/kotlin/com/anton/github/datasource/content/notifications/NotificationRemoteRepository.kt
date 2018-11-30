package com.anton.github.datasource.content.notifications

import com.anton.github.constants.BASE_GITHUB_API_URL
import com.anton.github.data.api.response.DetailsResponse
import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.GithubRestApi

interface NotificationRemoteRepository {

    suspend fun getNotifications(page: Int): List<Notification>

    suspend fun getDetails(url: String): DetailsResponse
}

class NotificationRemoteRepositoryImpl(private val api: GithubRestApi) : NotificationRemoteRepository {

    override suspend fun getNotifications(page: Int): List<Notification> {
        return api.getUserNotifications(page).await()
    }

    override suspend fun getDetails(url: String): DetailsResponse {
        return api.getDetails(url.replace(BASE_GITHUB_API_URL, "")).await()
    }
}