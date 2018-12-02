package com.anton.github.domain.usecase

import android.support.test.runner.AndroidJUnit4
import android.webkit.CookieManager
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.content.notifications.NotificationLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogoutUseCaseTest {

    lateinit var usecase: LogoutUseCase
    val userLocalRepository: UserLocalRepository = mock()
    val authLocalRepository: GithubAuthLocalRepository = mock()
    val notificationLocalRepository: NotificationLocalRepository = mock()

    @Before
    fun setup() {
        usecase = LogoutUseCaseImpl(userLocalRepository, authLocalRepository, notificationLocalRepository)
    }

    @Test
    fun run() {
        val result = runBlocking { usecase.run() }
        verify(userLocalRepository).userProfile = null
        verify(authLocalRepository).token = null
        verify(notificationLocalRepository).deleteAll()

        assertEquals(false, CookieManager.getInstance().hasCookies())
        assertEquals(CompletedUseCase, result)
    }
}