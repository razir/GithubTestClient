package com.anton.github.domain.usecase

import com.anton.github.data.entity.Notification
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import io.reactivex.Single

interface GetLocalNotificationsUseCase : BaseUseCase<List<Notification>>

class GetLocalNotificationsUseCaseImpl(private val notificationLocalRepository: NotificationLocalRepository) :
    GetLocalNotificationsUseCase {

    override fun run(): Single<List<Notification>> {
        return notificationLocalRepository.getNotifications()
    }

}