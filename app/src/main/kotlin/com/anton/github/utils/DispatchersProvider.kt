package com.anton.github.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object DispatchersProvider {
    var Main: CoroutineContext = Dispatchers.Main
    var IO: CoroutineContext = Dispatchers.IO

    fun setupTests() {
        Main = Dispatchers.Unconfined
        IO = Dispatchers.Unconfined
    }

    fun reset() {
        Main = Dispatchers.Main
        IO = Dispatchers.IO
    }

}