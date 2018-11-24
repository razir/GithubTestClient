package com.anton.medium.presentation.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anton.medium.presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startNext()
    }

    private fun startNext() {
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }
}