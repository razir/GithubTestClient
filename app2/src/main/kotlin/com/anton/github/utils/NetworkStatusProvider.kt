package com.anton.github.utils

import android.content.Context
import android.net.ConnectivityManager


interface NetworkStatusProvider {
    fun isNetworkAvailable(): Boolean
}

class NetworkStatusProviderImpl(private val context: Context) : NetworkStatusProvider {

    override fun isNetworkAvailable(): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connMgr.activeNetworkInfo?.isConnected ?: false
    }
}