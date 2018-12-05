package com.anton.github2.extensions

import android.support.annotation.IdRes
import com.anton.github2.R
import com.anton.github2.presentation.base.BaseActivity
import com.anton.github2.presentation.base.BaseFragment


fun BaseActivity.showFragment(
    fragment: BaseFragment, addToBackStack: Boolean = false,
    @IdRes id: Int = R.id.contentFrame, clearStack: Boolean = false
) {
    if (clearStack) {
        supportFragmentManager.popBackStack()
    }
    supportFragmentManager.beginTransaction().apply {
        replace(id, fragment)
        if (addToBackStack) {
            addToBackStack(null)
        }
        commitAllowingStateLoss()
    }
}

fun BaseFragment.showFragment(
    fragment: BaseFragment,
    addToBackStack: Boolean = false, @IdRes id: Int = R.id.contentFrame,
    clearStack: Boolean = false
) {
    if (activity is BaseActivity) {
        (activity as BaseActivity).showFragment(fragment, addToBackStack, id, clearStack)
    }
}