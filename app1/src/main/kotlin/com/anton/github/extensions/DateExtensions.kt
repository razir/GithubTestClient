package com.anton.github.extensions

import android.content.Context
import com.anton.github.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun String.parseDate(): Date? {
    try {
        val dateFormatter = SimpleDateFormat(FORMAT_API, Locale.getDefault())
        val date = dateFormatter.parse(this)
        return date
    } catch (e: ParseException) {
        return null
    }
}

fun Date.name(context: Context): String {
    val date = Calendar.getInstance()
    date.time = this

    val today = Calendar.getInstance().zeroTime()
    val yesterday = Calendar.getInstance().zeroTime().apply {
        add(Calendar.DAY_OF_MONTH, -1)
    }
    if (date.after(today)) {
        return context.getString(R.string.today)
    } else if (date.after(yesterday)) {
        return context.getString(R.string.yesterday)
    } else {
        val msDiff = Calendar.getInstance().timeInMillis - date.timeInMillis
        val daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
        return context.getString(R.string.days_past, daysDiff)
    }
}

fun Calendar.zeroTime(): Calendar {
    set(Calendar.HOUR, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}