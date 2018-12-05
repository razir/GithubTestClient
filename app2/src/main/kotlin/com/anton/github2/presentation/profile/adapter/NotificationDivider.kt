package com.anton.github2.presentation.profile.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import com.anton.github2.R

class NotificationDivider(context: Context) : DividerItemDecoration(context, DividerItemDecoration.VERTICAL) {

    init {
        setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_notifications)!!)
    }

}