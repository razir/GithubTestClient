package com.anton.github.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.*
import com.anton.github.presentation.profile.ProfileViewModel
import com.anton.github.utils.DispatchersProvider
import com.anton.github.utils.testObserver
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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

class ProfileViewModelTest {

    lateinit var viewModel: ProfileViewModel
    val logoutUseCase: LogoutUseCase = mock()
    val userProfileLocalRepository: UserLocalRepository = mock()
    val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase = mock()
    val testContext = TestCoroutineContext()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val testUserProfile = UserProfile().apply {
        name = "Test name"
        avatarUrl = "http://avatar_url"
        login = "test_login"
        followers = 10
        following = 150
    }

    private fun createViewModel() {
        viewModel = ProfileViewModel(
            userProfileLocalRepository,
            getRemoteUserProfileUseCase,
            logoutUseCase
        )
    }

    @Before
    fun setup() {
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun logoutClick() {
        createViewModel()
        val showLogoutConfirm = viewModel.getShowLogoutConfirm().testObserver()
        viewModel.logoutClick()

        assertEquals(1, showLogoutConfirm.observedValues.size)
    }

    @Test
    fun logoutConfirmed() {
        createViewModel()
        val showLogin = viewModel.getShowLogin().testObserver()
        viewModel.logoutConfirmed()
        testContext.triggerActions()

        assertEquals(1, showLogin.observedValues.size)
        runBlocking { verify(logoutUseCase).run() }
    }

    @Test
    fun loadProfileRemoteFailed() {

        whenever(runBlocking { getRemoteUserProfileUseCase.run() }).thenAnswer {
            throw IOException()
        }
        whenever(userProfileLocalRepository.userProfile).doReturn(testUserProfile)
        createViewModel()
        val username = viewModel.getUserName().testObserver()
        val userPicture = viewModel.getUserPicture().testObserver()
        val userNickname = viewModel.getUserNickname().testObserver()
        val followersCount = viewModel.getFollowersCount().testObserver()
        val followingCount = viewModel.getFollowingCount().testObserver()

        testContext.triggerActions()

        assertEquals(listOf(testUserProfile.login), userNickname.observedValues)
        assertEquals(listOf(testUserProfile.avatarUrl), userPicture.observedValues)
        assertEquals(listOf(testUserProfile.name), username.observedValues)
        assertEquals(listOf(testUserProfile.followers), followersCount.observedValues)
        assertEquals(listOf(testUserProfile.following), followingCount.observedValues)
    }

    @Test
    fun loadProfileRemoteSuccess() {
        val testRemoteProfile = UserProfile().apply {
            name = "Test name remote"
            avatarUrl = "http://avatar_url_remote"
            login = "test_login_remot"
            followers = 101
            following = 1501
        }

        whenever(runBlocking { getRemoteUserProfileUseCase.run() }).thenReturn(SuccessUseCase(testRemoteProfile))
        whenever(userProfileLocalRepository.userProfile).doReturn(testUserProfile)

        createViewModel()

        val username = viewModel.getUserName().testObserver()
        val userPicture = viewModel.getUserPicture().testObserver()
        val userNickname = viewModel.getUserNickname().testObserver()
        val followersCount = viewModel.getFollowersCount().testObserver()
        val followingCount = viewModel.getFollowingCount().testObserver()
        testContext.triggerActions()

        assertEquals(listOf(testUserProfile.login, testRemoteProfile.login), userNickname.observedValues)
        assertEquals(listOf(testUserProfile.avatarUrl, testRemoteProfile.avatarUrl), userPicture.observedValues)
        assertEquals(listOf(testUserProfile.name, testRemoteProfile.name), username.observedValues)
        assertEquals(listOf(testUserProfile.followers, testRemoteProfile.followers), followersCount.observedValues)
        assertEquals(listOf(testUserProfile.following, testRemoteProfile.following), followingCount.observedValues)
    }


}