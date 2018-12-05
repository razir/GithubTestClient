package com.anton.github.presentation.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anton.github.presentation.base.BaseActivity
import com.anton.github.presentation.login.LoginActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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
//        startActivity(ProfileActivity.getStartIntent(this))
        finish()
    }
}