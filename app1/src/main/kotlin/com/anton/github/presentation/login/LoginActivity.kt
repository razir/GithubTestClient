package com.anton.github.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.anton.github.R
import com.anton.github.extensions.showFragment
import com.anton.github.presentation.base.BaseActivity
import com.anton.github.presentation.login.start.NotLoggedFragment

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