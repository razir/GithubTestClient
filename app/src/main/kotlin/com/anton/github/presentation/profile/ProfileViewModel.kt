package com.anton.github.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.*
import com.anton.github.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val userProfileLocalRepository: UserLocalRepository,
    private val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
    private val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase,
    private val getLocalNotificationsUseCase: GetLocalNotificationsUseCase,
    private val getDetailsUrlUseCase: GetDetailsUrlUseCase,
    private val logoutUseCase: LogoutUseCase
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
    private var openDetails = SingleLiveEvent<String>()
    private var detailsLoadingError = SingleLiveEvent<Unit>()
    private var detailsLoading = MutableLiveData<Boolean>()
    private var showLogoutConfirm = SingleLiveEvent<Unit>()
    private var showLogin = SingleLiveEvent<Unit>()

    private var job = Job()
    private var detailsJob: Job? = null
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
    fun getOpenDetails(): LiveData<String> = openDetails
    fun getDetailsLoading(): LiveData<Boolean> = detailsLoading
    fun getDetailsLoadingError(): LiveData<Unit> = detailsLoadingError
    fun getShowLogoutConfirm(): LiveData<Unit> = showLogoutConfirm
    fun getShowLogin(): LiveData<Unit> = showLogin

    init {
        loadProfileFromCache()
        loadProfileFromRemote()
        loadNotificationsFromRemote()
    }

    fun refreshNotifications() {
        loadNotificationsFromRemote()
    }

    fun cancelLoadingDetails() {
        detailsJob?.cancel()
        detailsLoading.value = false
    }

    fun logoutClick() {
        showLogoutConfirm.call()
    }

    fun logoutConfirmed() {
        launch(Dispatchers.IO) {
            logoutUseCase.run()
            launch(Dispatchers.Main) {
                showLogin.call()
            }
        }
    }

    fun getDetails(notification: Notification) {
        detailsJob?.cancel()
        detailsJob = Job(job)
        notification.subject?.url?.let {
            detailsLoading.value = true
            launch(detailsJob!! + Dispatchers.IO) {
                delay(3000)
                val result = getDetailsUrlUseCase.run(it)
                launch(Dispatchers.Main) {
                    detailsLoading.value = false
                    when (result) {
                        is SuccessUseCase<String> -> openDetails.value = result.result
                        is ErrorUseCase<*> -> detailsLoadingError.call()
                    }
                }
            }
        }
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