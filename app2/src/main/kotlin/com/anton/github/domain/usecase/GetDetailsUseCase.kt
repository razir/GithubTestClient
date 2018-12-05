package com.anton.github.domain.usecase

import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import io.reactivex.Single

interface GetDetailsUrlUseCase {
    suspend fun run(url: String): Single<String>
}

class GetDetailsUseUrlCaseImpl(private val notificationRemoteRepository: NotificationRemoteRepository) :
    GetDetailsUrlUseCase {

    override suspend fun run(url: String): Single<String> {
        return notificationRemoteRepository.getDetails(url)
            .map {
                it.htmlUrl
            }
    }

}