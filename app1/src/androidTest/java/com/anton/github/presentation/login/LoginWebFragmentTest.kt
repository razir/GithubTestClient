package com.anton.github.presentation.login

import android.app.Activity
import android.app.Instrumentation
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import android.webkit.WebView
import com.agoda.kakao.*
import com.anton.github.R
import com.anton.github.constants.GITHUB_OAUTH_REDIRECT_URL
import com.anton.github.domain.usecase.*
import com.anton.github.extensions.showFragment
import com.anton.github.presentation.login.start.NotLoggedFragment
import com.anton.github.presentation.login.web.LoginWebViewFragment
import com.anton.github.presentation.profile.ProfileActivity
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.NetworkStatusProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class LoginWebFragmentTest : KoinComponent {
    @get:Rule
    val activityRule = IntentsTestRule(LoginActivity::class.java, true, true)
    val mockedNetworkProvider: NetworkStatusProvider = mock()
    val loginUrlComposerUseCase: LoginUrlComposerUseCase = mock()
    val loginUrlCallbackHandlerUseCase: LoginCallbackHandlerUseCase = mock()
    val authorizeUseCase: AuthorizeUseCase = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        DispatchersProvider.setupTests()
        val mockModule = module {
            single(override = true) { mockedNetworkProvider }
            factory(override = true) { loginUrlComposerUseCase }
            factory(override = true) { loginUrlCallbackHandlerUseCase }
            factory(override = true) { authorizeUseCase }
        }
        loadKoinModules(mockModule)
        activityRule.activity.showFragment(LoginWebViewFragment.newInstance())
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun testOpenPageNoConnection() {
        val screen = LoginWebScreen()
        screen {
            loginWebViewProgress.isNotDisplayed()
            loginWebviewErrorLogo.isDisplayed()
            loginWebView.isNotDisplayed()
        }
    }

    @Test
    fun testRefreshNoConnection() {
        val screen = LoginWebScreen()
        screen {
            loginWebviewErrorText.click()
            loginWebViewProgress.isNotDisplayed()
            loginWebviewErrorLogo.isDisplayed()
            loginWebviewErrorText.isDisplayed()
            loginWebView.isNotDisplayed()
        }
    }

    @Test
    fun testRefreshHaveConnection() {
        val screen = LoginWebScreen()
        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(false)
        screen {
            loginWebviewErrorText.isDisplayed()
        }
        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(true)
        whenever(loginUrlComposerUseCase.compose()).thenReturn("https://google.com")
        screen {
            loginWebviewErrorText.click()
            loginWebViewProgress.isDisplayed()
            idle(3000)
            loginWebView.isDisplayed()
            loginWebViewProgress.isNotDisplayed()
            loginWebviewErrorLogo.isGone()
            loginWebviewErrorText.isGone()
        }
    }

    @Test
    fun handleSuccessCallbackTokenFailed() {
        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(false)
        val screen = LoginWebScreen()
        screen {
            loginWebviewErrorText.isDisplayed()
        }

        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(true)
        whenever(loginUrlComposerUseCase.compose()).thenReturn("https://google.com")
        whenever(loginUrlCallbackHandlerUseCase.handle(any())).thenReturn(SuccessUseCase("code"))
        whenever(runBlocking { authorizeUseCase.run(any()) }).thenReturn(ErrorUseCase())

        screen {
            loginWebviewErrorText.click()
            idle()
        }
        activityRule.runOnUiThread {
            activityRule.activity.findViewById<WebView>(R.id.loginWebView)
                .loadUrl("javascript: window.location = \"$GITHUB_OAUTH_REDIRECT_URL\";")
        }
        screen.idle()
        val currentFragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.contentFrame)
        assertTrue(currentFragment is NotLoggedFragment)
    }

    @Test
    fun handleSuccessCallbackTokenSuccess() {
        val targetIntent = ProfileActivity.getStartIntent(activityRule.activity).component
        intending(hasComponent(targetIntent)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(false)
        val screen = LoginWebScreen()
        screen {
            loginWebviewErrorText.isDisplayed()
        }
        whenever(mockedNetworkProvider.isNetworkAvailable()).thenReturn(true)
        whenever(loginUrlComposerUseCase.compose()).thenReturn("https://google.com")
        whenever(loginUrlCallbackHandlerUseCase.handle(any())).thenReturn(SuccessUseCase("code"))
        whenever(runBlocking { authorizeUseCase.run(any()) }).thenReturn(CompletedUseCase)

        screen {
            loginWebviewErrorText.click()
        }
        screen.idle()
        activityRule.runOnUiThread {
            activityRule.activity.findViewById<WebView>(R.id.loginWebView)
                .loadUrl("javascript: window.location = \"$GITHUB_OAUTH_REDIRECT_URL\";")
        }
        screen.idle()
        intended(hasComponent(targetIntent))
    }

}

class LoginWebScreen : Screen<LoginWebScreen>() {
    val loginWebView = KView { withId(R.id.loginWebView) }
    val loginWebViewProgress = KProgressBar { withId(R.id.loginWebViewProgress) }
    val loginWebviewErrorLogo = KImageView { withId(R.id.loginWebviewErrorLogo) }
    val loginWebviewErrorText = KTextView { withId(R.id.loginWebViewError) }
}