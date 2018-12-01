package com.anton.github.presentation.profile

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
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
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AlertDialog
import android.text.style.StyleSpan
import com.anton.github.presentation.login.LoginActivity
import saschpe.android.customtabs.WebViewFallback
import saschpe.android.customtabs.CustomTabsHelper


class ProfileActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    private val viewModel: ProfileViewModel by inject()
    private lateinit var notificationsAdapter: NotificationsAdapter

    private var snackbarNotifications: Snackbar? = null
    private var snackbarDetails: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews()
        initViewModel()
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
        viewModel.getOpenDetails().observe(this, ObserverNotNull { url ->
            openUrl(url)
        })

        viewModel.getDetailsLoading().observe(this, ObserverNotNull { visible ->
            profileProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })

        viewModel.getDetailsLoadingError().observe(this, Observer {
            showSnackbarDetailsError()
        })

        viewModel.getShowLogoutConfirm().observe(this, Observer {
            showConfirmLogout()
        })

        viewModel.getShowLogin().observe(this, Observer {
            startActivity(LoginActivity.getStartIntent(this))
            finish()
        })
    }

    private fun initViews() {
        notificationsAdapter = NotificationsAdapter {
            viewModel.getDetails(it)
        }
        profileNotificationsList.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = notificationsAdapter
            addItemDecoration(NotificationDivider(this@ProfileActivity))
        }
        profileNotificationsErrorTxt.setOnClickListener {
            viewModel.refreshNotifications()
        }
        profileProgressCancel.setOnClickListener {
            viewModel.cancelLoadingDetails()
        }
        profileLogout.setOnClickListener {
            viewModel.logoutClick()
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
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            formattedValue.length,
            0
        )
        return spannable
    }

    private fun showSnackbarDetailsError() {
        snackbarDetails = Snackbar.make(profileProgress, R.string.snackbar_details_error, Snackbar.LENGTH_LONG)
        snackbarDetails?.show()
    }

    private fun showSnackbarNotiFromCache() {
        snackbarNotifications =
                Snackbar.make(profileNotificationsList, R.string.snackbar_noti_from_cache, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.refresh) {
                        viewModel.refreshNotifications()
                    }
        snackbarNotifications?.show()
    }

    private fun dismissSnackbar() {
        snackbarNotifications?.dismiss()
    }

    private fun showConfirmLogout() {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_logout_title)
            .setMessage(R.string.confirm_logout_message)
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.logoutConfirmed()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun openUrl(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(Color.WHITE)
            .setShowTitle(true)
            .build()
        val uri = Uri.parse(url)

        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(this, customTabsIntent, uri, WebViewFallback())
    }
}