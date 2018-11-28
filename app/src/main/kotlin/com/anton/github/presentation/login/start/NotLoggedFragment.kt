package com.anton.github.presentation.login.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anton.github.R
import com.anton.github.extensions.showFragment
import com.anton.github.presentation.base.BaseFragment
import com.anton.github.presentation.login.web.LoginWebViewFragment
import kotlinx.android.synthetic.main.fragment_not_logged.*

class NotLoggedFragment : BaseFragment() {

    companion object {
        fun newInstance(): NotLoggedFragment {
            return NotLoggedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_not_logged, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginStartBtn.setOnClickListener {
            showFragment(LoginWebViewFragment.newInstance(), true)
        }
    }

}