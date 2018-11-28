package com.anton.github.presentation.profile

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.anton.github.R
import com.anton.github.extensions.loadImage
import com.anton.github.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    private val viewModel: ProfileViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getUserPicture().observe(this, Observer { url ->
            url?.let {
                profileUserImg.loadImage(url)
            }
        })
        viewModel.getUserName().observe(this, Observer { name ->
            profileUserName.text = name
        })

        viewModel.getUserNickname().observe(this, Observer { nickname ->
            profileUserNickName.text = nickname
        })
    }
}