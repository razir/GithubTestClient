package com.anton.github.extensions

import java.text.DecimalFormat

fun Int.formatNumberWithComma(): String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(this.toLong())
}