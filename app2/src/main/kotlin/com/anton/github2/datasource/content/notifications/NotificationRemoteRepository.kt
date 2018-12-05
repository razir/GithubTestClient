package com.anton.github2.datasource.content.notifications

import com.anton.github2.constants.BASE_GITHUB_API_URL
import com.anton.github2.data.api.response.DetailsResponse
import com.anton.github2.data.entity.Notification
import com.anton.github2.datasource.content.GithubRestApi
import io.reactivex.Single

interface NotificationRemoteRepository {

    fun getNotifications(page: Int): Single<List<Notification>>

    fun getDetails(url: String): Single<DetailsResponse>
}

class NotificationRemoteRepositoryImpl(private val api: GithubRestApi) : NotificationRemoteRepository {

    override fun getNotifications(page: Int): Single<List<Notification>> {
        return api.getUserNotifications(page)
    }

    override fun getDetails(url: String): Single<DetailsResponse> {
        return api.getDetails(url.replace(BASE_GITHUB_API_URL, ""))
    }
}