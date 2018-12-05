package com.anton.github2.extensions

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObjectPreferenceDelegate<T>(
    private val sp: SharedPreferences,
    val key: String,
    val serialize: (T?) -> String?,
    val deserialize: (String?) -> T?
) : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return deserialize(sp.getString(key, null))
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        if (value == null) {
            sp.edit().remove(key).apply()
        } else {
            sp.edit().putString(key, serialize(value)).apply()
        }
    }
}

inline fun <reified T> json(sp: SharedPreferences, key: String): ObjectPreferenceDelegate<T> {
    return ObjectPreferenceDelegate(sp, key, { it.toJson() }, { it?.parse<T>() })
}