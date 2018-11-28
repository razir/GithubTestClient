package com.anton.github.presentation.login.web

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.*
import com.anton.github.R
import com.anton.github.extensions.showFragment
import com.anton.github.presentation.base.BaseFragment
import com.anton.github.presentation.login.start.NotLoggedFragment
import com.anton.github.utils.ObserverNotNull
import kotlinx.android.synthetic.main.fragment_login_webview.*
import org.koin.android.ext.android.inject
import android.webkit.*
import com.anton.github.presentation.profile.ProfileActivity


class LoginWebViewFragment : BaseFragment() {

    private val viewModel: LoginWebViewModel by inject()

    companion object {
        fun newInstance(): LoginWebViewFragment {
            return LoginWebViewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebViewClient()
        viewModel.getLoadUrl().observe(viewLifecycleOwner, Observer { url ->
            loginWebView.loadUrl(url)
        })

        viewModel.getShowProgress().observe(viewLifecycleOwner, ObserverNotNull { visible ->
            loginWebViewProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })

        viewModel.getShowLoginRequired().observe(viewLifecycleOwner, ObserverNotNull { show ->
            showFragment(NotLoggedFragment.newInstance(), clearStack = true)
        })
        viewModel.getShowProfile().observe(viewLifecycleOwner, Observer {
            context?.let {
                startActivity(ProfileActivity.getStartIntent(it))
            }
        })
    }

    private fun setupWebViewClient() {
        loginWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                viewModel.handleUrl(url)
                return true;
            }

            @TargetApi(21)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                viewModel.handleUrl(request?.url.toString())
                return true
            }
        }
        loginWebView.settings.apply {
            javaScriptEnabled = true
        }
        loginWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() === MotionEvent.ACTION_UP
                && loginWebView.canGoBack()
            ) {
                loginWebView.goBack()
                return@OnKeyListener true
            } else {
                false
            }
        })
    }
}