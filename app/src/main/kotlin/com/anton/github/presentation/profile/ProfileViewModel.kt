package com.anton.github.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.ErrorUseCase
import com.anton.github.domain.usecase.GetNotificationsUseCase
import com.anton.github.domain.usecase.GetRemoteUserProfileUseCase
import com.anton.github.domain.usecase.SuccessUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val userProfileLocalRepository: UserLocalRepository,
    private val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel(),
    CoroutineScope {

    private var userPicture = MutableLiveData<String>()
    private var userName = MutableLiveData<String>()
    private var userNickname = MutableLiveData<String>()
    private var notifications = MutableLiveData<List<Notification>>()
    private var notificationsError = MutableLiveData<Boolean>()
    private var notificationsLoading = MutableLiveData<Boolean>()

    private var job = Job()
    override val coroutineContext: CoroutineContext = job

    fun getUserPicture(): LiveData<String> = userPicture
    fun getUserName(): LiveData<String> = userName
    fun getUserNickname(): LiveData<String> = userNickname
    fun getNotifications(): LiveData<List<Notification>> = notifications
    fun getNotificationsError(): LiveData<Boolean> = notificationsError
    fun getNotificationsLoading(): LiveData<Boolean> = notificationsLoading

    init {
        loadProfileFromCache()
        loadProfileFromRemote()
        loadNotificationsFromRemote()
    }

    fun refresh() {
        loadProfileFromRemote()
        loadNotificationsFromRemote()
    }

    private fun loadProfileFromRemote() {
        launch(Dispatchers.IO) {
            val profileData = getRemoteUserProfileUseCase.run()
            launch(Dispatchers.Main) {
                if (profileData is SuccessUseCase<UserProfile>) {
                    val userProfile = profileData.result
                    setProfileInfo(userProfile)
                }
            }
        }
    }

    private fun loadProfileFromCache() {
        val profile = userProfileLocalRepository.userProfile
        profile?.let {
            setProfileInfo(it)
        }
    }

    private fun loadNotificationsFromRemote() {
        notificationsError.value = false
        notificationsLoading.value = true
        launch(Dispatchers.IO) {
            val data = getNotificationsUseCase.run(0)
            launch(Dispatchers.Main) {
                notificationsLoading.value = false
                when (data) {
                    is SuccessUseCase<List<Notification>> -> {
                        notificationsError.value = false
                        notifications.value = data.result
                    }
                    is ErrorUseCase<*> -> notificationsError.value = true
                }
            }
        }
    }

    private fun setProfileInfo(profile: UserProfile) {
        userName.value = profile.name
        userPicture.value = profile.avatarUrl
        userNickname.value = profile.login
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}