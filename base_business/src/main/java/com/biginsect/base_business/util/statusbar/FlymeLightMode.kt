package com.biginsect.base_business.util.statusbar

import android.app.Activity
import android.view.WindowManager
import java.lang.Exception

/**
 *@author biginsect
 *Created at 2021/3/22 19:26
 */
class FlymeLightMode : IStatusBarLightMode {

    override fun setLightMode(activity: Activity, isFontDark: Boolean): Boolean {
        val window = activity.window
        var result: Boolean
        try {
            val params = window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlag = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlag.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlag.getInt(params)
            value = if (isFontDark) {
                value.or(bit)
            } else {
                value.and(bit.inv())
            }
            meizuFlag.setInt(params, value)
            window.attributes = params
            result = true
        } catch (e: Exception) {
            result = false
        }

        return result
    }
}