package com.anton.github.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val userProfileLocalRepository: UserLocalRepository,
    private val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
    private val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase,
    private val getLocalNotificationsUseCase: GetLocalNotificationsUseCase
) : ViewModel(),
    CoroutineScope {

    private var userPicture = MutableLiveData<String>()
    private var userName = MutableLiveData<String>()
    private var userNickname = MutableLiveData<String>()
    private var notifications = MutableLiveData<List<Notification>>()
    private var emptyNotificationsError = MutableLiveData<Boolean>()
    private var cachedNotificationsWarning = MutableLiveData<Boolean>()
    private var notificationsLoading = MutableLiveData<Boolean>()
    private var followersCount = MutableLiveData<Int>()
    private var followingCount = MutableLiveData<Int>()

    private var job = Job()
    override val coroutineContext: CoroutineContext = job

    fun getUserPicture(): LiveData<String> = userPicture
    fun getUserName(): LiveData<String> = userName
    fun getUserNickname(): LiveData<String> = userNickname
    fun getNotifications(): LiveData<List<Notification>> = notifications
    fun getEmptyNotificationsError(): LiveData<Boolean> = emptyNotificationsError
    fun getCachedNotificationsWarning(): LiveData<Boolean> = cachedNotificationsWarning
    fun getNotificationsLoading(): LiveData<Boolean> = notificationsLoading
    fun getFollowersCount(): LiveData<Int> = followersCount
    fun getFollowingCount(): LiveData<Int> = followingCount

    init {
        loadProfileFromCache()
        loadProfileFromRemote()
        loadNotificationsFromRemote()
    }

    fun refreshNotifications() {
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

    private fun loadNotificationsFromCache() {
        launch(Dispatchers.IO) {
            val cachedNotiResult = getLocalNotificationsUseCase.run()
            launch(Dispatchers.Main) {
                notificationsLoading.value = false
                val cachedNoti = (cachedNotiResult as SuccessUseCase<List<Notification>>).result
                if (cachedNoti.isEmpty()) {
                    emptyNotificationsError.value = true
                } else {
                    cachedNotificationsWarning.value = true
                    notifications.value = cachedNoti
                }
            }
        }
    }

    private fun loadNotificationsFromRemote() {
        emptyNotificationsError.value = false
        cachedNotificationsWarning.value = false
        notificationsLoading.value = true
        launch(Dispatchers.IO) {
            val data = getRemoteNotificationsUseCase.run(0)
            launch(Dispatchers.Main) {
                when (data) {
                    is SuccessUseCase<List<Notification>> -> {
                        notificationsLoading.value = false
                        emptyNotificationsError.value = false
                        cachedNotificationsWarning.value = false
                        notifications.value = data.result
                    }
                    is ErrorUseCase<*> -> {
                        data.e?.printStackTrace()
                        loadNotificationsFromCache()
                    }
                }
            }
        }
    }

    private fun setProfileInfo(profile: UserProfile) {
        userName.value = profile.name
        userPicture.value = profile.avatarUrl
        userNickname.value = profile.login
        followersCount.value = profile.followers
        followingCount.value = profile.following
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}