package com.anton.github.presentation.login

import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import com.agoda.kakao.KButton
import com.agoda.kakao.Screen
import com.anton.github.R
import com.anton.github.extensions.showFragment
import com.anton.github.presentation.login.start.NotLoggedFragment
import com.anton.github.presentation.login.web.LoginWebViewFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotLoggedFragmentTest {
    @get:Rule
    val activityRule = IntentsTestRule(LoginActivity::class.java, true, true)

    @Before
    fun setup() {
        activityRule.activity.showFragment(NotLoggedFragment.newInstance())
    }

    @Test
    fun loginBtnClick() {
        val screen = NotLoggedScreen()
        screen {
            loginBtn {
                click()
            }
        }
        val currentFragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.contentFrame)
        assertEquals(1, activityRule.activity.supportFragmentManager.backStackEntryCount)
        assertTrue(currentFragment is LoginWebViewFragment)
    }

}

class NotLoggedScreen : Screen<NotLoggedScreen>() {
    val loginBtn = KButton { withId(R.id.loginStartBtn) }
}