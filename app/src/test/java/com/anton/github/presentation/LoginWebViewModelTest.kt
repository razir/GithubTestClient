package com.anton.github.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.github.constants.GITHUB_OAUTH_REDIRECT_URL
import com.anton.github.domain.usecase.*
import com.anton.github.presentation.login.web.LoginWebViewModel
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.NetworkStatusProvider
import com.anton.github.utils.testObserver
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginWebViewModelTest {

    lateinit var viewModel: LoginWebViewModel
    val loginUrlComposerUseCase: LoginUrlComposerUseCase = mock()
    val loginCallbackHandlerUseCase: LoginCallbackHandlerUseCase = mock()
    val authorizeUseCase: AuthorizeUseCase = mock()
    val networkStatusProvider: NetworkStatusProvider = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val testUrl = "http://anton_test"
    val testRedirectUrl = "http://anton_test_redirect"

    @Before
    fun setup() {
        DispatchersProvider.setupTests()
        doReturn(testUrl).whenever(loginUrlComposerUseCase).compose()
        whenever(networkStatusProvider.isNetworkAvailable()).doReturn(true)
        viewModel = LoginWebViewModel(
            loginUrlComposerUseCase, loginCallbackHandlerUseCase,
            authorizeUseCase, networkStatusProvider
        )
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun init() {
        val showProgress = viewModel.getShowProgress().testObserver()
        val loadUrl = viewModel.getLoadUrl().testObserver()

        assertEquals(listOf(true), showProgress.observedValues)
        assertEquals(listOf(testUrl), loadUrl.observedValues)
    }

    @Test
    fun handleUrlWithCodeError() {
        val showWebError = viewModel.getShowWebError().testObserver()
        val loadUrl = viewModel.getLoadUrl().testObserver()

        doReturn(ErrorUseCase<String>()).whenever(loginCallbackHandlerUseCase).handle(any())
        viewModel.handleUrl("$GITHUB_OAUTH_REDIRECT_URL/somepath")

        assertEquals(listOf(testUrl), loadUrl.observedValues)
        assertEquals(listOf(true), showWebError.observedValues)
        runBlocking {
            verify(authorizeUseCase, never()).run(any())
        }
    }

    @Test
    fun handleUrlWithCodeTokenError() {
        val showWebError = viewModel.getShowWebError().testObserver()
        val loadUrl = viewModel.getLoadUrl().testObserver()
        val tokenError = viewModel.getShowTokenError().testObserver()

        whenever(runBlocking { authorizeUseCase.run(any()) }).thenReturn(ErrorUseCase())
        doReturn(SuccessUseCase("code")).whenever(loginCallbackHandlerUseCase).handle(any())
        runBlocking {
            viewModel.handleUrl("$GITHUB_OAUTH_REDIRECT_URL/somepath")
        }
        assertEquals(listOf(testUrl), loadUrl.observedValues)
        assertEquals(0, showWebError.observedValues.size)
        assertEquals(1, tokenError.observedValues.size)
    }

    @Test
    fun handleUrlWithCodeTokenSuccess() {
        val showWebError = viewModel.getShowWebError().testObserver()
        val loadUrl = viewModel.getLoadUrl().testObserver()
        val showProfile = viewModel.getShowProfile().testObserver()

        whenever(runBlocking { authorizeUseCase.run(any()) }).thenReturn(CompletedUseCase)

        doReturn(SuccessUseCase("code")).whenever(loginCallbackHandlerUseCase).handle(any())
        viewModel.handleUrl("$GITHUB_OAUTH_REDIRECT_URL/somepath")

        assertEquals(listOf(testUrl), loadUrl.observedValues)
        assertEquals(0, showWebError.observedValues.size)
        assertEquals(1, showProfile.observedValues.size)
    }

    @Test
    fun handleOtherUrl() {
        val loadUrl = viewModel.getLoadUrl().testObserver()

        viewModel.handleUrl("http://some_random_url")

        assertEquals(listOf(testUrl, "http://some_random_url"), loadUrl.observedValues)
    }

    @Test
    fun handleError() {
        val showProgress = viewModel.getShowProgress().testObserver()
        val showWebError = viewModel.getShowWebError().testObserver()
        val showWebView = viewModel.getShowWebView().testObserver()

        viewModel.handleError()
        assertEquals(listOf(true, false), showProgress.observedValues)
        assertEquals(listOf(true), showWebError.observedValues)
        assertEquals(listOf(false), showWebView.observedValues)
    }

    @Test
    fun handleRefresh() {
        val loadUrl = viewModel.getLoadUrl().testObserver()
        val showProgress = viewModel.getShowProgress().testObserver()
        val showWebError = viewModel.getShowWebError().testObserver()

        viewModel.handleRefresh()
        assertEquals(listOf(true, true), showProgress.observedValues)
        assertEquals(listOf(false), showWebError.observedValues)
        assertEquals(listOf(testUrl, testUrl), loadUrl.observedValues)
    }

    @Test
    fun redirectScenarioClean() {
        val loadUrl = viewModel.getLoadUrl().testObserver()
        val showProgress = viewModel.getShowProgress().testObserver()
        val showWebView = viewModel.getShowWebView().testObserver()

        viewModel.handleUrl(testRedirectUrl)
        viewModel.handleLoadingFinished(testUrl)
        viewModel.handleLoadingFinished(testRedirectUrl)

        assertEquals(listOf(true, false), showProgress.observedValues)
        assertEquals(listOf(testUrl, testRedirectUrl), loadUrl.observedValues)
        assertEquals(1, showWebView.observedValues.size)
    }

    @Test
    fun redirectScenarioAfterError() {
        val loadUrl = viewModel.getLoadUrl().testObserver()
        val showProgress = viewModel.getShowProgress().testObserver()
        val showWebView = viewModel.getShowWebView().testObserver()
        val showWebError = viewModel.getShowWebError().testObserver()

        viewModel.handleError()
        viewModel.handleRefresh()
        viewModel.handleUrl(testRedirectUrl)
        viewModel.handleLoadingFinished(testUrl)
        viewModel.handleLoadingFinished(testRedirectUrl)

        assertEquals(listOf(true, false, true, false), showProgress.observedValues)
        assertEquals(listOf(testUrl, testUrl, testRedirectUrl), loadUrl.observedValues)
        assertEquals(listOf(false, true), showWebView.observedValues)
        assertEquals(listOf(true, false), showWebError.observedValues)
    }
}