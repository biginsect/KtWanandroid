package com.biginsect.base_business.util.statusbar

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager

/**
 *@author biginsect
 *Created at 2021/3/22 18:08
 */
open class CommonLightMode : IStatusBarLightMode {

    override fun setLightMode(activity: Activity, isFontDark: Boolean): Boolean {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
        var option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (isFontDark) {
            option = option.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        window.decorView.systemUiVisibility = option
        return true
    }
}