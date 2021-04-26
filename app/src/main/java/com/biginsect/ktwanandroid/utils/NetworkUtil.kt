package com.biginsect.ktwanandroid.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 *@author biginsect
 *Created at 2021/4/26 16:20
 */
object NetworkUtil {

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val networkCapabilities = cm.getNetworkCapabilities(network)
        return networkCapabilities != null
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }
}