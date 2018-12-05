package com.anton.github.presentation

import android.app.Activity
import android.app.Instrumentation
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.agoda.kakao.*
import com.anton.github.R
import com.anton.github.data.api.response.DetailsResponse
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.domain.usecase.*
import com.anton.github.extensions.name
import com.anton.github.extensions.parseDate
import com.anton.github.presentation.login.LoginActivity
import com.anton.github.presentation.profile.ProfileActivity
import com.anton.github.readFromFile
import com.anton.github.utils.DispatchersProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(ProfileActivity::class.java, true, false)
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val userProfileLocalRepository: UserLocalRepository = mock()
    val getRemoteUserProfileUseCase: GetRemoteUserProfileUseCase = mock()
    val logoutUseCase: LogoutUseCase = mock()
    val getRemoteNotificationsUseCase: GetRemoteNotificationsUseCase = mock()
    val getLocalNotificationsUseCase: GetLocalNotificationsUseCase = mock()
    val getDetailsUrlUseCase: GetDetailsUrlUseCase = mock()

    val context = InstrumentationRegistry.getTargetContext()

    val testUser = UserProfile().apply {
        name = "test name"
        id = "some_id"
        login = "test_login"
        url = "test_url"
        avatarUrl = "avatar_url"
        followers = 10
        following = 20
    }

    @Before
    fun setup() {
        DispatchersProvider.setupTests()
        val mockModule = module {
            factory(override = true) { userProfileLocalRepository }
            factory(override = true) { getRemoteUserProfileUseCase }
            factory(override = true) { logoutUseCase }
            factory(override = true) { getRemoteNotificationsUseCase }
            factory(override = true) { getLocalNotificationsUseCase }
            factory(override = true) { getDetailsUrlUseCase }
        }
        loadKoinModules(mockModule)
        whenever(userProfileLocalRepository.userProfile).thenReturn(testUser)
        whenever(runBlocking { getRemoteUserProfileUseCase.run() }).thenReturn(SuccessUseCase(testUser))
    }

    @After
    fun reset() {
        DispatchersProvider.reset()
    }

    @Test
    fun checkProfileData() {
        activityRule.launchActivity(null)
        val screen = ProfileScreen()

        screen {
            idle()
            profileUserNickName {
                isDisplayed()
                hasText(testUser.login!!)
            }
            profileUserImg.isDisplayed()
            profileFollowers {
                isDisplayed()
                hasText("${testUser.followers} ${context.getString(R.string.followers)}")
            }
            profileFollowing {
                isDisplayed()
                hasText("${testUser.following} ${context.getString(R.string.following)}")
            }
        }
    }

    @Test
    fun checkNotificationsFromRemote() {
        val testContext = TestCoroutineContext()
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext

        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).doReturn(SuccessUseCase(testNoti))
        activityRule.launchActivity(null)

        val screen = ProfileScreen()
        val firstNoti = testNoti[0]

        screen {
            progressBar.isVisible()
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            progressBar.isInvisible()
            profileNotificationsList {
                isVisible()
                firstChild<ProfileScreen.Item> {
                    title {
                        isVisible()
                        hasText(firstNoti.subject?.title!!)
                    }
                    repoName {
                        isVisible()
                        hasText(firstNoti.repository?.name!!)
                    }
                    date {
                        isVisible()
                        hasText(firstNoti.updatedAt?.parseDate()?.name(context)!!)
                    }
                    badge {
                        isVisible()
                        hasAnyText()
                    }
                }
            }

        }
    }

    @Test
    fun checkNotificationsFromCache() {
        val testContext = TestCoroutineContext()
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext

        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).doReturn(ErrorUseCase())
        whenever(runBlocking { getLocalNotificationsUseCase.run() }).doReturn(SuccessUseCase(testNoti))
        activityRule.launchActivity(null)

        val screen = ProfileScreen()

        screen {
            progressBar.isVisible()
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            progressBar.isInvisible()
            profileNotificationsList.isVisible()
            snackbarNotificationsFromCache.isVisible()
        }
    }

    @Test
    fun detailsLoadingFailed() {
        val testContext = TestCoroutineContext()
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext

        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).doReturn(SuccessUseCase(testNoti))
        whenever(runBlocking { getDetailsUrlUseCase.run(any()) }).doReturn(ErrorUseCase())
        activityRule.launchActivity(null)

        val screen = ProfileScreen()

        screen {
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            profileNotificationsList {
                isVisible()
                firstChild<ProfileScreen.Item> {
                    click()
                }
            }
            detailsProgress.isVisible()
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            detailsProgress.isInvisible()
            snackbarDetailsError.isVisible()
        }
    }

    @Test
    fun detailsLoadingSuccess() {
        val testContext = TestCoroutineContext()
        DispatchersProvider.Main = testContext
        DispatchersProvider.IO = testContext


        val testNoti: List<Notification> = readFromFile("notifications_list_example")
        whenever(runBlocking { getRemoteNotificationsUseCase.run(any()) }).doReturn(SuccessUseCase(testNoti))

        val htmlUrl = "http://test_url"

        whenever(runBlocking { getDetailsUrlUseCase.run(any()) }).doReturn(SuccessUseCase(htmlUrl))
        activityRule.launchActivity(null)
        intending(hasPackage("com.android.chrome")).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val screen = ProfileScreen()

        screen {
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            profileNotificationsList {
                isVisible()
                firstChild<ProfileScreen.Item> {
                    click()
                }
            }
            activityRule.runOnUiThread {
                testContext.triggerActions()
            }
            detailsProgress.isInvisible()

            intended(hasPackage("com.android.chrome"))
        }
    }

    @Test
    fun profileLogout() {
        activityRule.launchActivity(null)
        val screen = ProfileScreen()
        screen {
            profileLogoutBtn.click()
            idle()
            confirmBtn.click()
        }
        runBlocking { verify(logoutUseCase).run() }
        intended(hasComponent(LoginActivity.getStartIntent(context).component))
    }
}

class ProfileScreen : Screen<ProfileScreen>() {
    val profileUserNickName = KTextView { withId(R.id.profileUserNickName) }
    val profileLogoutBtn = KView { withId(R.id.profileLogout) }
    val detailsProgress = KView { withId(R.id.profileProgress) }
    val profileFollowers = KTextView { withId(R.id.profileFollowers) }
    val profileFollowing = KTextView { withId(R.id.profileFollowing) }
    val profileUserImg = KImageView { withId(R.id.profileUserImg) }
    val progressBar = KProgressBar { withId(R.id.profileNotificationsProgress) }
    val snackbarDetailsError = KTextView { withText(R.string.snackbar_details_error) }
    val snackbarNotificationsFromCache = KTextView { withText(R.string.snackbar_noti_from_cache) }
    val confirmBtn = KTextView { withText(R.string.confirm) }

    val profileNotificationsList = KRecyclerView({
        withId(R.id.profileNotificationsList)
    }, itemTypeBuilder = {
        itemType(::Item)
    })

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val title: KTextView = KTextView(parent) { withId(R.id.notificationTitle) }
        val repoName: KTextView = KTextView(parent) { withId(R.id.notificationRepositoryName) }
        val date: KTextView = KTextView(parent) { withId(R.id.notificationDate) }
        val badge: KTextView = KTextView(parent) { withId(R.id.notificationBadge) }
    }
}