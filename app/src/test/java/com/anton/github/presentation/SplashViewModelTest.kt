package com.anton.github.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.github.data.api.response.TokenResponse
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.presentation.splash.SplashViewModel
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.testObserver
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.TimeUnit


class SplashViewModelTest {

    lateinit var viewModel: SplashViewModel
    val authLocalRepository: GithubAuthLocalRepository = mock()
    val testContext = TestCoroutineContext()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        DispatchersProvider.Main = testContext
        viewModel = SplashViewModel(authLocalRepository)
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun nextTokenNull() {
        doReturn(null).whenever(authLocalRepository).token

        val showLogin = viewModel.getShowLogin().testObserver()
        val showProfile = viewModel.getShowProfile().testObserver()

        viewModel.next()
        testContext.advanceTimeBy(2000, TimeUnit.MILLISECONDS)
        testContext.triggerActions()

        assertEquals(1, showLogin.observedValues.size)
        assertEquals(0, showProfile.observedValues.size)
    }

    @Test
    fun nextTokenNotNull() {
        doReturn(TokenResponse().apply {
            accessToken = "token"
        }).whenever(authLocalRepository).token

        val showLogin = viewModel.getShowLogin().testObserver()
        val showProfile = viewModel.getShowProfile().testObserver()

        viewModel.next()
        testContext.advanceTimeBy(2000, TimeUnit.MILLISECONDS)
        testContext.triggerActions()

        assertEquals(0, showLogin.observedValues.size)
        assertEquals(1, showProfile.observedValues.size)
    }

}