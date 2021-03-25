package com.biginsect.base_business.util

import android.app.Activity
import android.os.Build
import com.biginsect.base_business.util.statusbar.*
import java.util.*

/**
 *@author biginsect
 *Created at 2021/3/22 19:40
 */
object StatusBarUtils {

    private val Mi by lazy { MiLightMode() }
    private val Flyme by lazy { FlymeLightMode() }
    private val Oppo by lazy { OppoLightMode() }
    private val Common by lazy { CommonLightMode() }

    private fun create(): IStatusBarLightMode {
        return when {
            "xiaomi" in Build.MANUFACTURER.toLowerCase(Locale.getDefault()) -> Mi
            "flyme" in Build.MANUFACTURER.toLowerCase(Locale.getDefault()) -> Flyme
            "oppo" in Build.MANUFACTURER.toLowerCase(Locale.getDefault()) -> Oppo
            else -> Common
        }
    }

    @JvmStatic
    fun statusBarLightMode(activity: Activity, isFontDark: Boolean): Boolean {
        return create().setLightMode(activity, isFontDark)
    }
}