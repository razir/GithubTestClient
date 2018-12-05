package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import java.lang.Exception

interface GetRemoteNotificationsUseCase {
    suspend fun run(page: Int): ResultUseCase<List<Notification>>
}

class GetRemoteNotificationsUseCaseImpl(
    private val notificationRemoteRepository: NotificationRemoteRepository,
    private val notificationsLocalRepository: NotificationLocalRepository
) : GetRemoteNotificationsUseCase {

    override suspend fun run(page: Int): ResultUseCase<List<Notification>> {
        try {
            val notifications = notificationRemoteRepository.getNotifications(page)
            if (page == 0) {
                notificationsLocalRepository.saveNotifications(notifications)
            }
            return SuccessUseCase(notifications)
        } catch (e: Exception) {
            return ErrorUseCase(e = e)
        }
    }

}

