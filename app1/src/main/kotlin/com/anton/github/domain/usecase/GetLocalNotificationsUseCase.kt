package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository

interface GetLocalNotificationsUseCase : BaseUseCase<List<Notification>>

class GetLocalNotificationsUseCaseImpl(private val notificationLocalRepository: NotificationLocalRepository) :
    GetLocalNotificationsUseCase {

    override suspend fun run(): ResultUseCase<List<Notification>> {
        return SuccessUseCase(notificationLocalRepository.getNotifications())
    }

}