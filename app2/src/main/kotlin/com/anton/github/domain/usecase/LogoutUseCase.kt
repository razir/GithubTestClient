package com.anton.github.domain.usecase

import android.webkit.CookieManager
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepository
import io.reactivex.Completable


interface LogoutUseCase {
    fun run(): Completable
}

class LogoutUseCaseImpl(
    private val userLocalRepository: UserLocalRepository,
    private val authLocalRepository: GithubAuthLocalRepository,
    private val notificationLocalRepository: NotificationLocalRepository
) : LogoutUseCase {

    override fun run(): Completable {
        return Completable.fromCallable {
            userLocalRepository.userProfile = null
            authLocalRepository.token = null
            CookieManager.getInstance().removeAllCookies(null)
        }.andThen(notificationLocalRepository.deleteAll())
    }
}