package com.anton.github.domain.usecase

import com.anton.github.datasource.content.notifications.NotificationRemoteRepository

interface GetDetailsUrlUseCase {
    suspend fun run(url: String): ResultUseCase<String>
}

class GetDetailsUseUrlCaseImpl(private val notificationRemoteRepository: NotificationRemoteRepository) :
    GetDetailsUrlUseCase {

    override suspend fun run(url: String): ResultUseCase<String> {
        try {
            val detailsUrl = notificationRemoteRepository.getDetails(url).htmlUrl
            if (detailsUrl != null) {
                return SuccessUseCase(detailsUrl)
            } else {
                return ErrorUseCase()
            }
        } catch (e: Exception) {
            return ErrorUseCase(e = e)
        }
    }

}