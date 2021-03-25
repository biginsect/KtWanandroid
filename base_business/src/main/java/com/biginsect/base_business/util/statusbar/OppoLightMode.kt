package com.biginsect.base_business.util.statusbar

import android.app.Activity
import android.view.View

/**
 *@author biginsect
 *Created at 2021/3/22 19:37
 */
class OppoLightMode : IStatusBarLightMode {

    override fun setLightMode(activity: Activity, isFontDark: Boolean): Boolean {
        val statusBarTint = if (isFontDark) {
            0x00000010
        } else {
            0x00190000
        }
        val decorView = activity.window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or statusBarTint
        return true
    }
}