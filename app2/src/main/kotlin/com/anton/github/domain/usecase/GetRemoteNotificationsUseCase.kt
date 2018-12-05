package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.notifications.NotificationRemoteRepository
import io.reactivex.Single

interface GetRemoteNotificationsUseCase {
    fun run(page: Int): Single<List<Notification>>
}

class GetRemoteNotificationsUseCaseImpl(
    private val notificationRemoteRepository: NotificationRemoteRepository,
    private val notificationsLocalRepository: NotificationLocalRepository
) : GetRemoteNotificationsUseCase {

    override fun run(page: Int): Single<List<Notification>> {
        return notificationRemoteRepository.getNotifications(page)
            .doOnSuccess {
                if (page == 0) {
                    notificationsLocalRepository.saveNotifications(it)
                }
            }
    }

}

