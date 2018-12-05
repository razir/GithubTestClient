package com.anton.github.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Int.formatNumber(): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.groupingSeparator = ' '
    val formatter = DecimalFormat()
    formatter.groupingSize = 3
    formatter.decimalFormatSymbols = symbols

    return formatter.format(this.toLong())
}