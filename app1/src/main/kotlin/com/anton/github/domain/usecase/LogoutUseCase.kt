package com.anton.github.domain.usecase

import android.webkit.CookieManager
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepository


interface LogoutUseCase : BaseUseCase<Unit>

class LogoutUseCaseImpl(
    private val userLocalRepository: UserLocalRepository,
    private val authLocalRepository: GithubAuthLocalRepository,
    private val notificationLocalRepository: NotificationLocalRepository
) : LogoutUseCase {

    override suspend fun run(): ResultUseCase<Unit> {
        userLocalRepository.userProfile = null
        authLocalRepository.token = null
        notificationLocalRepository.deleteAll()
        CookieManager.getInstance().removeAllCookies(null)
        return CompletedUseCase
    }
}