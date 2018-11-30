package com.anton.github.presentation.profile

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.anton.github.R
import com.anton.github.extensions.formatNumberWithComma
import com.anton.github.extensions.loadImage
import com.anton.github.presentation.base.BaseActivity
import com.anton.github.presentation.profile.adapter.NotificationDivider
import com.anton.github.presentation.profile.adapter.NotificationsAdapter
import com.anton.github.utils.ObserverNotNull
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    private val viewModel: ProfileViewModel by inject()
    private lateinit var notificationsAdapter: NotificationsAdapter

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel.getUserPicture().observe(this, Observer { url ->
            url?.let {
                profileUserImg.loadImage(url)
            }
        })
        viewModel.getUserName().observe(this, Observer { name ->
            profileUserName.visibility = if (name == null) View.GONE else View.VISIBLE
            profileUserName.text = name

        })

        viewModel.getUserNickname().observe(this, Observer { nickname ->
            profileUserNickName.text = nickname
        })

        viewModel.getNotifications().observe(this, Observer { notifications ->
            notifications?.let {
                notificationsAdapter.notifications = it
            }
        })
        viewModel.getFollowersCount().observe(this, ObserverNotNull { count ->
            profileFollowers.text = createValueSpan(count, "Followers")
        })
        viewModel.getFollowingCount().observe(this, ObserverNotNull { count ->
            profileFollowing.text = createValueSpan(count, "Following")
        })

        viewModel.getNotificationsLoading().observe(this, ObserverNotNull { visible ->
            profileNotificationsProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })
        viewModel.getEmptyNotificationsError().observe(this, ObserverNotNull { visible ->
            showError(visible)
        })

        viewModel.getCachedNotificationsWarning().observe(this, ObserverNotNull { visible ->
            if (visible) {
                showSnackbarNotiFromCache()
            } else {
                dismissSnackbar()
            }
        })
    }

    private fun initViews() {
        notificationsAdapter = NotificationsAdapter {

        }
        profileNotificationsList.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = notificationsAdapter
            addItemDecoration(NotificationDivider(this@ProfileActivity))
        }
        profileNotificationsErrorTxt.setOnClickListener {
            viewModel.refreshNotifications()
        }
    }

    private fun showError(visible: Boolean) {
        profileNotificationsErrorGroup.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun createValueSpan(value: Int, name: String): SpannableString {
        val formattedValue = value.formatNumberWithComma()

        val spannable = SpannableString("$formattedValue $name")
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorTextPrimary)),
            0,
            formattedValue.length,
            0
        )
        return spannable
    }

    private fun showSnackbarNotiFromCache() {
        snackbar =
                Snackbar.make(profileNotificationsList, R.string.snackbar_noti_from_cache, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.refresh) {
                        viewModel.refreshNotifications()
                    }
        snackbar?.show()
    }

    private fun dismissSnackbar() {
        snackbar?.dismiss()
    }
}