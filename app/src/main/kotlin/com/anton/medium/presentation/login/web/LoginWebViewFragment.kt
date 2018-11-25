package com.anton.medium.presentation.login.web

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.anton.medium.R
import com.anton.medium.extensions.showFragment
import com.anton.medium.presentation.base.BaseFragment
import com.anton.medium.presentation.login.start.NotLoggedFragment
import com.anton.medium.utils.ObserverNotNull
import kotlinx.android.synthetic.main.fragment_login_webview.*
import org.koin.android.ext.android.inject

private const val USER_AGENT =
    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"

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
            userAgentString = USER_AGENT
        }
    }
}