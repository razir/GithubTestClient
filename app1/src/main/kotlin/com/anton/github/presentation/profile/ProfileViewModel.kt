package com.anton.github.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.GetRemoteUserProfileUseCase
import com.anton.github.domain.usecase.LogoutUseCase
import com.anton.github.domain.usecase.SuccessUseCase
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val userProfileLocalRepository: UserLocalRepository,
    private val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel(),
    CoroutineScope {

    private var userPicture = MutableLiveData<String>()
    private var userName = MutableLiveData<String>()
    private var userNickname = MutableLiveData<String>()
    private var followersCount = MutableLiveData<Int>()
    private var followingCount = MutableLiveData<Int>()

    private var showLogoutConfirm = SingleLiveEvent<Unit>()
    private var showLogin = SingleLiveEvent<Unit>()

    private var job = Job()
    override val coroutineContext: CoroutineContext = job

    fun getUserPicture(): LiveData<String> = userPicture
    fun getUserName(): LiveData<String> = userName
    fun getUserNickname(): LiveData<String> = userNickname
    fun getFollowersCount(): LiveData<Int> = followersCount
    fun getFollowingCount(): LiveData<Int> = followingCount
    fun getShowLogoutConfirm(): LiveData<Unit> = showLogoutConfirm
    fun getShowLogin(): LiveData<Unit> = showLogin

    init {
        loadProfileFromCache()
        loadProfileFromRemote()
    }

    fun logoutClick() {
        showLogoutConfirm.call()
    }

    fun logoutConfirmed() {
        launch(DispatchersProvider.IO) {
            logoutUseCase.run()
            launch(DispatchersProvider.Main) {
                showLogin.call()
            }
        }
    }

    private fun loadProfileFromRemote() {
        launch(DispatchersProvider.IO) {
            val profileData = getRemoteUserProfileUseCase.run()
            launch(DispatchersProvider.Main) {
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