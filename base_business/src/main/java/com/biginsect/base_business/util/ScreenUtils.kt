package com.biginsect.base_business.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 *@author biginsect
 *Created at 2021/3/18 15:03
 */
object ScreenUtils {

    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    @JvmStatic
    fun getScreenRealHeight(context: Context): Int {
        return getRealSize(context, true)

    }

    @JvmStatic
    fun getScreenRealWidth(context: Context): Int {
        return getRealSize(context, false)
    }

    @JvmStatic
    private fun getRealSize(context: Context, isHeight: Boolean): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
        if (isHeight) {
            return point.y
        }
        return point.x
    }
}