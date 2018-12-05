package com.anton.github.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import com.anton.github.data.api.response.TokenResponse
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.presentation.login.LoginActivity
import com.anton.github.presentation.profile.ProfileActivity
import com.anton.github.presentation.splash.SplashActivity
import com.anton.github.utils.DispatchersProvider
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class SplashActivityTest : KoinComponent {

    @get:Rule
    val activityRule = IntentsTestRule(SplashActivity::class.java, true, false)
    val testContext = TestCoroutineContext()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun openLogin() {
        DispatchersProvider.Main = testContext
        val authRepo = get<GithubAuthLocalRepository>()
        authRepo.token = null

        activityRule.launchActivity(null)
        testContext.advanceTimeBy(2000, TimeUnit.MILLISECONDS)
        testContext.triggerActions()

        val targetIntent = LoginActivity.getStartIntent(InstrumentationRegistry.getTargetContext())
        intended(hasComponent(targetIntent.component))
    }

    @Test
    fun openProfile() {
        DispatchersProvider.Main = testContext
        val authRepo = get<GithubAuthLocalRepository>()
        authRepo.token = TokenResponse().apply {
            accessToken = "accessToken"
        }

        activityRule.launchActivity(null)
        testContext.advanceTimeBy(2000, TimeUnit.MILLISECONDS)
        testContext.triggerActions()

        val targetIntent = ProfileActivity.getStartIntent(InstrumentationRegistry.getTargetContext())
        intended(hasComponent(targetIntent.component))
    }
}