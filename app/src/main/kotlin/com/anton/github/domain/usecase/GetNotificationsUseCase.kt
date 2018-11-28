package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.publication.PublicationRemoteRepository

interface GetNotificationsUseCase {
    suspend fun run(page: Int): ResultUseCase<List<Notification>>
}

class GetNotificationsUseCaseImpl(
    private val publicationRemoteRepository: PublicationRemoteRepository
) : GetNotificationsUseCase {

    override suspend fun run(page: Int): ResultUseCase<List<Notification>> {
        val notifications = publicationRemoteRepository.getNotifications(page)
        return SuccessUseCase(notifications)
    }

}

