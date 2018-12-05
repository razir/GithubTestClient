package com.anton.github2.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.anton.github2.R
import com.anton.github2.extensions.showFragment
import com.anton.github2.presentation.base.BaseActivity
import com.anton.github2.presentation.login.start.NotLoggedFragment


class LoginActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            showFragment(NotLoggedFragment.newInstance())
        }
    }
}