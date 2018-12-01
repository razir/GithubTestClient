package com.anton.github.presentation.login.web

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.constants.GITHUB_OAUTH_REDIRECT_URL
import com.anton.github.domain.usecase.*
import com.anton.github.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginWebViewModel(
    private val loginUrlComposerUseCase: LoginUrlComposerUseCase,
    private val loginUrlCallbackHandlerUseCase: LoginCallbackHandlerUseCase,
    private val authorizeUseCase: AuthorizeUseCase
) : ViewModel(), CoroutineScope {
    private val showProgress = MutableLiveData<Boolean>()
    private val loadUrl = SingleLiveEvent<String>()
    private val showTokenError = SingleLiveEvent<String>()
    private val showProfile = SingleLiveEvent<Unit>()
    private val showWebError = MutableLiveData<Boolean>()
    private val showWebView = MutableLiveData<Boolean>()

    private var job = Job()
    override val coroutineContext: CoroutineContext = job

    fun getShowProgress(): LiveData<Boolean> = showProgress
    fun getShowWebView(): LiveData<Boolean> = showWebView
    fun getLoadUrl(): LiveData<String> = loadUrl
    fun getShowProfile(): LiveData<Unit> = showProfile
    fun getShowWebError(): LiveData<Boolean> = showWebError
    fun getShowTokenError(): LiveData<String> = showTokenError
    private var lastRedirectUrl: String? = null

    init {
        showProgress.value = true
        loadUrl(loginUrlComposerUseCase.compose())
    }

    fun handleUrl(url: String?) {
        if (url?.startsWith(GITHUB_OAUTH_REDIRECT_URL) == true) {
            val result = loginUrlCallbackHandlerUseCase.handle(url)
            when (result) {
                is ErrorUseCase<*> -> showWebError.value = true
                is SuccessUseCase<String> -> getToken(result.result)
            }
        } else {
            loadUrl(url)
        }
    }

    fun handleError() {
        showProgress.value = false
        showWebError.value = true
        showWebView.value = false
    }

    fun handleRefresh() {
        showWebError.value = false
        showProgress.value = true
        loadUrl(loginUrlComposerUseCase.compose())
    }

    fun handleLoadingFinished(url: String?) {
        if (lastRedirectUrl == url) {
            showProgress.value = false
            if (showWebError.value != true) {
                showWebView.value = true
            }
        }
    }

    private fun loadUrl(url: String?) {
        lastRedirectUrl = url
        loadUrl.value = url
    }

    private fun getToken(code: String) {
        launch(Dispatchers.IO) {
            val result = authorizeUseCase.run(code)
            launch(Dispatchers.Main) {
                when (result) {
                    is ErrorUseCase -> showTokenError.value = result.msg
                    is CompletedUseCase -> showProfile.call()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}