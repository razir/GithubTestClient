package com.anton.github.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.Subject
import com.anton.github.domain.usecase.*
import com.anton.github.presentation.profile.NotificationsViewModel
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.testObserver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

class NotificationsViewModelTest {

    lateinit var viewModel: NotificationsViewModel
    val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase = mock()
    val getLocalNotificationsUseCase: GetLocalNotificationsUseCase = mock()
    val getDetailsUrlUseCase: GetDetailsUrlUseCase = mock()

    val testContext = TestCoroutineContext()
    val testNoti = Notification().apply {
        subject = Subject().apply {
            url = "http://test_url"
        }
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    private fun createViewModel() {
        viewModel = NotificationsViewModel(
            getRemoteNotificationsUseCase,
            getLocalNotificationsUseCase,
            getDetailsUrlUseCase
        )
    }

    @Test
    fun getDetailsFailed() {
        createViewModel()
        whenever(runBlocking { getDetailsUrlUseCase.run(any()) }).thenReturn(ErrorUseCase())

        val detailsLoading = viewModel.getDetailsLoading().testObserver()
        val detailsLoadingError = viewModel.getDetailsLoadingError().testObserver()

        viewModel.getDetails(testNoti)

        testContext.triggerActions()
        assertEquals(listOf(true, false), detailsLoading.observedValues)
        assertEquals(1, detailsLoadingError.observedValues.size)
    }

    @Test
    fun getDetailsSuccess() {
        createViewModel()
        whenever(runBlocking { getDetailsUrlUseCase.run(any()) }).thenReturn(SuccessUseCase("test_url"))

        val detailsLoading = viewModel.getDetailsLoading().testObserver()
        val openDetails = viewModel.getOpenDetails().testObserver()

        viewModel.getDetails(testNoti)

        testContext.triggerActions()
        assertEquals(listOf(true, false), detailsLoading.observedValues)
        assertEquals(1, openDetails.observedValues.size)
    }

    @Test
    fun cancelLoadingDetails() {
        createViewModel()
        whenever(runBlocking { getDetailsUrlUseCase.run(any()) }).thenReturn(SuccessUseCase("test_url"))

        val detailsLoading = viewModel.getDetailsLoading().testObserver()
        val openDetails = viewModel.getOpenDetails().testObserver()
        val detailsLoadingError = viewModel.getDetailsLoadingError().testObserver()

        viewModel.getDetails(testNoti)
        viewModel.cancelLoadingDetails()

        testContext.triggerActions()

        assertEquals(listOf(true, false), detailsLoading.observedValues)
        assertEquals(0, openDetails.observedValues.size)
        assertEquals(0, detailsLoadingError.observedValues.size)
    }

    @Test
    fun initRemoteFailedLocalEmpty() {
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).thenReturn(ErrorUseCase())
        whenever(runBlocking { getLocalNotificationsUseCase.run() }).thenReturn(SuccessUseCase(listOf()))

        createViewModel()
        val emptyNotificationError = viewModel.getEmptyNotificationsError().testObserver()
        val cachedNotificationsWarning = viewModel.getCachedNotificationsWarning().testObserver()
        val notificationsLoading = viewModel.getNotificationsLoading().testObserver()
        val notifications = viewModel.getNotifications().testObserver()

        testContext.triggerActions()

        assertEquals(listOf(false, true), emptyNotificationError.observedValues)
        assertEquals(listOf(false), cachedNotificationsWarning.observedValues)
        assertEquals(listOf(true, false), notificationsLoading.observedValues)
        assertEquals(0, notifications.observedValues.size)
    }

    @Test
    fun initRemoteFailedLocalNotEmpty() {
        val testNotifications = listOf(Notification())
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).thenReturn(ErrorUseCase())
        whenever(runBlocking { getLocalNotificationsUseCase.run() }).thenReturn(SuccessUseCase(testNotifications))

        createViewModel()
        val emptyNotificationError = viewModel.getEmptyNotificationsError().testObserver()
        val cachedNotificationsWarning = viewModel.getCachedNotificationsWarning().testObserver()
        val notificationsLoading = viewModel.getNotificationsLoading().testObserver()
        val notifications = viewModel.getNotifications().testObserver()

        testContext.triggerActions()

        assertEquals(listOf(false), emptyNotificationError.observedValues)
        assertEquals(listOf(false, true), cachedNotificationsWarning.observedValues)
        assertEquals(listOf(true, false), notificationsLoading.observedValues)
        assertEquals(testNotifications, notifications.observedValues[0])
        assertEquals(1, notifications.observedValues.size)
    }

    @Test
    fun initRemoteSuccess() {
        val testNotifications = listOf(Notification())
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).thenReturn(SuccessUseCase(testNotifications))

        createViewModel()
        val emptyNotificationError = viewModel.getEmptyNotificationsError().testObserver()
        val cachedNotificationsWarning = viewModel.getCachedNotificationsWarning().testObserver()
        val notificationsLoading = viewModel.getNotificationsLoading().testObserver()
        val notifications = viewModel.getNotifications().testObserver()

        testContext.triggerActions()

        assertEquals(listOf(false), emptyNotificationError.observedValues)
        assertEquals(listOf(false), cachedNotificationsWarning.observedValues)
        assertEquals(listOf(true, false), notificationsLoading.observedValues)
        assertEquals(testNotifications, notifications.observedValues[0])
        assertEquals(1, notifications.observedValues.size)
    }
}