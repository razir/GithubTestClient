package com.anton.medium.presentation.login.web

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.medium.constants.AUTH_MEDIUM_REDIRECT_URL
import com.anton.medium.domain.usecase.*
import com.anton.medium.utils.SingleLiveEvent
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

    init {
        showProgress.value = false
        loadUrl.value = loginUrlComposerUseCase.compose()
    }

    fun handleUrl(url: String?) {
        if (url?.startsWith(AUTH_MEDIUM_REDIRECT_URL) == true) {
            val result = loginUrlCallbackHandlerUseCase.handle(url)
            when (result) {
                is ErrorUseCase -> showLoginRequited.call()
                is SuccessUseCase<*> -> getToken(result.result as String)
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