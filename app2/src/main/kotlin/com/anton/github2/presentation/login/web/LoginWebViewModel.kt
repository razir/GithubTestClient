package com.anton.github2.presentation.login.web

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github2.constants.GITHUB_OAUTH_REDIRECT_URL
import com.anton.github2.domain.usecase.AuthorizeUseCase
import com.anton.github2.domain.usecase.LoginCallbackHandlerUseCase
import com.anton.github2.domain.usecase.LoginUrlComposerUseCase
import com.anton.github2.extensions.addTo
import com.anton.github2.utils.NetworkStatusProvider
import com.anton.github2.utils.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginWebViewModel(
    private val loginUrlComposerUseCase: LoginUrlComposerUseCase,
    private val loginUrlCallbackHandlerUseCase: LoginCallbackHandlerUseCase,
    private val authorizeUseCase: AuthorizeUseCase,
    private val networkStatusProvider: NetworkStatusProvider

) : ViewModel() {
    private val showProgress = MutableLiveData<Boolean>()
    private val loadUrl = SingleLiveEvent<String>()
    private val showTokenError = SingleLiveEvent<String>()
    private val showProfile = SingleLiveEvent<Unit>()
    private val showWebError = MutableLiveData<Boolean>()
    private val showWebView = MutableLiveData<Boolean>()

    fun getShowProgress(): LiveData<Boolean> = showProgress
    fun getShowWebView(): LiveData<Boolean> = showWebView
    fun getLoadUrl(): LiveData<String> = loadUrl
    fun getShowProfile(): LiveData<Unit> = showProfile
    fun getShowWebError(): LiveData<Boolean> = showWebError
    fun getShowTokenError(): LiveData<String> = showTokenError

    private var lastRedirectUrl: String? = null
    private val disposable = CompositeDisposable()

    init {
        if (networkStatusProvider.isNetworkAvailable()) {
            showProgress.value = true
            loadUrl(loginUrlComposerUseCase.compose())
        } else {
            handleError()
        }
    }

    fun handleUrl(url: String?) {
        if (url?.startsWith(GITHUB_OAUTH_REDIRECT_URL) == true) {
            loginUrlCallbackHandlerUseCase.handle(url)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    showWebError.value = true
                }
                .flatMapCompletable {
                    getToken(it)
                }
                .onErrorComplete()
                .subscribe()
                .addTo(disposable)

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
        if (networkStatusProvider.isNetworkAvailable()) {
            showWebError.value = false
            showProgress.value = true
            loadUrl(loginUrlComposerUseCase.compose())
        }
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

    private fun getToken(code: String): Completable {
        return authorizeUseCase.run(code)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                showProfile.call()
            }
            .doOnError {
                showTokenError.value = it.message
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}