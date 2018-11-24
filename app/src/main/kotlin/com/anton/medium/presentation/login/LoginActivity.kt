package com.anton.medium.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.anton.medium.R
import com.anton.medium.presentation.base.BaseActivity

class LoginActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}