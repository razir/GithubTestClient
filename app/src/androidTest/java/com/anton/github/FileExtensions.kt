package com.anton.github

import com.anton.github.extensions.parse

inline fun <reified T> Any.readFromFile(filePath: String): T {
    val input = javaClass.getClassLoader().getResourceAsStream(filePath)
    val json = String(input.readBytes())
    return json.parse<T>()
}