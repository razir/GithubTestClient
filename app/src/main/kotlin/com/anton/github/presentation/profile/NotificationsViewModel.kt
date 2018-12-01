package com.anton.github.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anton.github.data.entity.Notification
import com.anton.github.domain.usecase.*
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NotificationsViewModel(
    private val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase,
    private val getLocalNotificationsUseCase: GetLocalNotificationsUseCase,
    private val getDetailsUrlUseCase: GetDetailsUrlUseCase
) : ViewModel(), CoroutineScope {

    private var notifications = MutableLiveData<List<Notification>>()
    private var emptyNotificationsError = MutableLiveData<Boolean>()
    private var cachedNotificationsWarning = MutableLiveData<Boolean>()
    private var notificationsLoading = MutableLiveData<Boolean>()
    private var openDetails = SingleLiveEvent<String>()
    private var detailsLoadingError = SingleLiveEvent<Unit>()
    private var detailsLoading = MutableLiveData<Boolean>()

    private var job = Job()
    private var detailsJob: Job? = null
    override val coroutineContext: CoroutineContext = job

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
        detailsJob?.cancel()
        detailsLoading.value = false
    }

    fun refreshNotifications() {
        loadNotificationsFromRemote()
    }

    fun getDetails(notification: Notification) {
        detailsJob?.cancel()
        detailsJob = Job(job)
        notification.subject?.url?.let {
            detailsLoading.value = true
            launch(detailsJob!! + DispatchersProvider.IO) {
                val result = getDetailsUrlUseCase.run(it)
                launch(DispatchersProvider.Main) {
                    detailsLoading.value = false
                    when (result) {
                        is SuccessUseCase<String> -> openDetails.value = result.result
                        is ErrorUseCase<*> -> detailsLoadingError.call()
                    }
                }
            }
        }
    }

    private fun loadNotificationsFromCache() {
        launch(DispatchersProvider.IO) {
            val cachedNotiResult = getLocalNotificationsUseCase.run()
            launch(DispatchersProvider.Main) {
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
        launch(DispatchersProvider.IO) {
            val data = getRemoteNotificationsUseCase.run(0)
            launch(DispatchersProvider.Main) {
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}