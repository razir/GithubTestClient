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
    loginUrlComposerUseCase: LoginUrlComposerUseCase,
    private val loginUrlCallbackHandlerUseCase: LoginCallbackHandlerUseCase,
    private val authorizeUseCase: AuthorizeUseCase
) : ViewModel(), CoroutineScope {
    private val showProgress = MutableLiveData<Boolean>()
    private val loadUrl = SingleLiveEvent<String>()
    private val showError = SingleLiveEvent<String>()
    private val showLoginRequited = SingleLiveEvent<Unit>()
    private val showProfile = SingleLiveEvent<Unit>()

    private var job = Job()
    override val coroutineContext: CoroutineContext = job

    fun getShowProgress(): LiveData<Boolean> = showProgress
    fun getLoadUrl(): LiveData<String> = loadUrl
    fun getShowLoginRequired(): LiveData<Unit> = showLoginRequited
    fun getShowProfile(): LiveData<Unit> = showProfile

    init {
        showProgress.value = false
        loadUrl.value = loginUrlComposerUseCase.compose()
    }

    fun handleUrl(url: String?) {
        if (url?.startsWith(GITHUB_OAUTH_REDIRECT_URL) == true) {
            val result = loginUrlCallbackHandlerUseCase.handle(url)
            when (result) {
                is ErrorUseCase<*> -> showLoginRequited.call()
                is SuccessUseCase<String> -> getToken(result.result)
            }
        } else {
            loadUrl.value = url
        }
    }

    private fun getToken(code: String) {
        launch(Dispatchers.IO) {
            val result = authorizeUseCase.run(code)
            launch(Dispatchers.Main) {
                when (result) {
                    is ErrorUseCase -> showError.value = result.msg
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