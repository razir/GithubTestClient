package com.anton.github2.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github2.data.entity.UserProfile
import com.anton.github2.datasource.content.user.UserLocalRepository
import com.anton.github2.domain.usecase.GetRemoteUserProfileUseCase
import com.anton.github2.domain.usecase.LogoutUseCase
import com.anton.github2.extensions.addTo
import com.anton.github2.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val userProfileLocalRepository: UserLocalRepository,
    private val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private var userPicture = MutableLiveData<String>()
    private var userName = MutableLiveData<String>()
    private var userNickname = MutableLiveData<String>()
    private var followersCount = MutableLiveData<Int>()
    private var followingCount = MutableLiveData<Int>()

    private var showLogoutConfirm = SingleLiveEvent<Unit>()
    private var showLogin = SingleLiveEvent<Unit>()

    private val disposable = CompositeDisposable()

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
        logoutUseCase.run()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showLogin.call()
            }, {

            }).addTo(disposable)
    }

    private fun loadProfileFromRemote() {
        getRemoteUserProfileUseCase.run()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userProfile ->
                setProfileInfo(userProfile)
            }, {

            }).addTo(disposable)
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
        disposable.clear()
    }
}