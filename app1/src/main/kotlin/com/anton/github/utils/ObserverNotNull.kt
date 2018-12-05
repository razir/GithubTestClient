package com.anton.github.utils

import android.arch.lifecycle.Observer

class ObserverNotNull<T>(private val onChangedNoNull: ((t: T) -> Unit)) : Observer<T> {

    override fun onChanged(t: T?) {
        onChangedNoNull(t!!)
    }
}