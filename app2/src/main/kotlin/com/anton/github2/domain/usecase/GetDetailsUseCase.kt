package com.anton.github2.domain.usecase

import com.anton.github2.datasource.content.notifications.NotificationRemoteRepository
import io.reactivex.Single

interface GetDetailsUrlUseCase {
    fun run(url: String): Single<String>
}

class GetDetailsUseUrlCaseImpl(private val notificationRemoteRepository: NotificationRemoteRepository) :
    GetDetailsUrlUseCase {

    override fun run(url: String): Single<String> {
        return notificationRemoteRepository.getDetails(url)
            .map {
                it.htmlUrl
            }
    }

}