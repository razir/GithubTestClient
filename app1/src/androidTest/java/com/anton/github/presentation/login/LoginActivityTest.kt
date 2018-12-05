package com.anton.github.presentation.login

import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import com.anton.github.R
import com.anton.github.presentation.login.start.NotLoggedFragment
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = IntentsTestRule(LoginActivity::class.java, true, true)

    @Test
    fun run() {
        val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.contentFrame)
        assertTrue(fragment is NotLoggedFragment)
    }
}