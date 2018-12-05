package com.anton.github.utils

import android.arch.lifecycle.LiveData

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}