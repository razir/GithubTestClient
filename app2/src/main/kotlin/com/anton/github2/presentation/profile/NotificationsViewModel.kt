package com.anton.github2.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github2.data.entity.Notification
import com.anton.github2.domain.usecase.*
import com.anton.github2.extensions.addTo
import com.anton.github2.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationsViewModel(
    private val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase,
    private val getLocalNotificationsUseCase: GetLocalNotificationsUseCase,
    private val getDetailsUrlUseCase: GetDetailsUrlUseCase
) : ViewModel() {

    private var notifications = MutableLiveData<List<Notification>>()
    private var emptyNotificationsError = MutableLiveData<Boolean>()
    private var cachedNotificationsWarning = MutableLiveData<Boolean>()
    private var notificationsLoading = MutableLiveData<Boolean>()
    private var openDetails = SingleLiveEvent<String>()
    private var detailsLoadingError = SingleLiveEvent<Unit>()
    private var detailsLoading = MutableLiveData<Boolean>()

    private var detailsDisposable: Disposable? = null
    private val disposable = CompositeDisposable()

    fun getNotifications(): LiveData<List<Notification>> = notifications
    fun getEmptyNotificationsError(): LiveData<Boolean> = emptyNotificationsError
    fun getCachedNotificationsWarning(): LiveData<Boolean> = cachedNotificationsWarning
    fun getNotificationsLoading(): LiveData<Boolean> = notificationsLoading
    fun getOpenDetails(): LiveData<String> = openDetails
    fun getDetailsLoading(): LiveData<Boolean> = detailsLoading
    fun getDetailsLoadingError(): LiveData<Unit> = detailsLoadingError

    init {
        loadNotificationsFromRemote()
    }

    fun cancelLoadingDetails() {
        detailsDisposable?.dispose()
        detailsLoading.value = false
    }

    fun refreshNotifications() {
        loadNotificationsFromRemote()
    }

    fun getDetails(notification: Notification) {
        detailsDisposable?.dispose()
        notification.subject?.url?.let {
            detailsLoading.value = true
            detailsDisposable = getDetailsUrlUseCase.run(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    detailsLoading.value = false
                    openDetails.value = result
                }, {
                    detailsLoading.value = false
                    detailsLoadingError.call()
                })
        }
    }

    private fun loadNotificationsFromCache(): Single<List<Notification>> {
        return getLocalNotificationsUseCase.run()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { cachedNoti ->
                if (cachedNoti.isEmpty()) {
                    emptyNotificationsError.value = true
                } else {
                    cachedNotificationsWarning.value = true
                    notifications.value = cachedNoti
                }
            }
    }

    private fun loadNotificationsFromRemote() {
        emptyNotificationsError.value = false
        cachedNotificationsWarning.value = false
        notificationsLoading.value = true

        getRemoteNotificationsUseCase.run(0)
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { loadNotificationsFromCache() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                notificationsLoading.value = false
                notifications.value = it
            }, {

            }).addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}