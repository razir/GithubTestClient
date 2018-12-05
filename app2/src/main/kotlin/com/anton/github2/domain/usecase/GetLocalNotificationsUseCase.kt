package com.anton.github2.domain.usecase

import com.anton.github2.data.entity.Notification
import com.anton.github2.datasource.content.notifications.NotificationLocalRepository
import io.reactivex.Single

interface GetLocalNotificationsUseCase : BaseUseCase<List<Notification>>

class GetLocalNotificationsUseCaseImpl(private val notificationLocalRepository: NotificationLocalRepository) :
    GetLocalNotificationsUseCase {

    override fun run(): Single<List<Notification>> {
        return notificationLocalRepository.getNotifications()
    }

}