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
        initViews()
        initViewModel()
    }

    private fun initViews() {
        loginToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        loginToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        loginToolbar.setTitle(R.string.login_on_github)
        loginWebViewError.setOnClickListener {
            viewModel.handleRefresh()
        }
    }

    private fun initViewModel() {
        viewModel.getLoadUrl().observe(viewLifecycleOwner, Observer { url ->
            loginWebView.loadUrl(url)
        })

        viewModel.getShowProgress().observe(viewLifecycleOwner, ObserverNotNull { visible ->
            loginWebViewProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })

        viewModel.getShowTokenError().observe(viewLifecycleOwner, Observer {
            showFragment(NotLoggedFragment.newInstance(), clearStack = true)
        })
        viewModel.getShowProfile().observe(viewLifecycleOwner, Observer {
            context?.let {
                startActivity(ProfileActivity.getStartIntent(it))
            }
        })
        viewModel.getShowWebError().observe(viewLifecycleOwner, ObserverNotNull { visible ->
            loginWebErrorGroup.visibility = if (visible) View.VISIBLE else View.GONE
        })

        viewModel.getShowWebView().observe(viewLifecycleOwner, ObserverNotNull { visible ->
            loginWebView.visibility = if (visible) View.VISIBLE else View.GONE
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

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                viewModel.handleError()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModel.handleLoadingFinished(url)
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