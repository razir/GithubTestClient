package com.anton.github2.presentation.splash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.anton.github2.datasource.auth.GithubAuthLocalRepository
import com.anton.github2.extensions.addTo
import com.anton.github2.utils.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

private const val SPLASH_DELAY_MILLS = 2000L

class SplashViewModel(private val authLocalRepository: GithubAuthLocalRepository) : ViewModel() {

    private val showLogin = SingleLiveEvent<Unit>()
    private val showProfile = SingleLiveEvent<Unit>()

    fun getShowLogin(): LiveData<Unit> = showLogin
    fun getShowProfile(): LiveData<Unit> = showProfile
    private val disposable = CompositeDisposable()
    fun next() {
        Observable.timer(SPLASH_DELAY_MILLS, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (authLocalRepository.token?.accessToken != null) {
                    showProfile.call()
                } else {
                    showLogin.call()
                }
            }.addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}