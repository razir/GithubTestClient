package com.anton.github.presentation.splash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val SPLASH_DELAY_MILLS = 2000L

class SplashViewModel(private val authLocalRepository: GithubAuthLocalRepository) : ViewModel(), CoroutineScope {

    private val showLogin = SingleLiveEvent<Unit>()
    private val showProfile = SingleLiveEvent<Unit>()
    private val job = Job()

    override val coroutineContext: CoroutineContext = job

    fun getShowLogin(): LiveData<Unit> = showLogin
    fun getShowProfile(): LiveData<Unit> = showProfile

    fun next() {
        launch(Dispatchers.Main) {
            delay(SPLASH_DELAY_MILLS)
            if (authLocalRepository.token != null) {
                showProfile.call()
            } else {
                showLogin.call()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}