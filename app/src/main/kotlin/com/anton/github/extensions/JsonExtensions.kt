package com.anton.github.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun <T> T.toJson(gson: Gson = Gson()): String = gson.toJson(this)

inline fun <reified T> String.parse(gson: Gson = Gson()): T = gson.fromJson(this, object : TypeToken<T>() {}.type)