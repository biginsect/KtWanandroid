package com.biginsect.ktwanandroid.util

import android.content.Context
import android.content.SharedPreferences
import com.biginsect.ktwanandroid.constant.Constants
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *@author biginsect
 *Created at 2021/3/25 19:41
 */
class Preferences<T>(private val key: String, private val value: T) : ReadWriteProperty<Any?, T> {

    companion object {
        lateinit var preference: SharedPreferences

        fun init(context: Context) {
            preference = context.getSharedPreferences(
                context.packageName + Constants.SP_NAME,
                Context.MODE_PRIVATE
            )
        }

        fun clear() {
            preference.edit().clear().apply()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = find(key, value)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = put(key, value)

    @Suppress("UNCHECKED_CAST")
    private fun <F> find(key: String, value: F): F = with(preference) {
        val res = when (value) {
            is Long -> getLong(key, value)
            is Int -> getInt(key, value)
            is Boolean -> getBoolean(key, value)
            is Float -> getFloat(key, value)
            is String -> getString(key, value)
            else -> throw IllegalArgumentException("Do not support this type.")
        }
        res as F
    }

    private fun <F> put(key: String, value: F) = with(preference.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is String -> putString(key, value)
            else -> throw IllegalArgumentException("Do not support this type.")
        }.apply()
    }
}