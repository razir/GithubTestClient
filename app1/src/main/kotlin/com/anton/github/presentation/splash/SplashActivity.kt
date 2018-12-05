package com.anton.github.presentation.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anton.github.presentation.login.LoginActivity
import com.anton.github.presentation.profile.ProfileActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        viewModel.next()
    }

    private fun initViewModel() {
        viewModel.getShowLogin().observe(this, Observer {
            showLogin()
        })

        viewModel.getShowProfile().observe(this, Observer {
            showProfile()
        })
    }

    private fun showLogin() {
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }

    private fun showProfile() {
        startActivity(ProfileActivity.getStartIntent(this))
        finish()
    }
}