package com.biginsect.base_business.util.statusbar

import android.app.Activity

/**
 *@author biginsect
 *Created at 2021/3/22 18:07
 */
class MiLightMode: CommonLightMode() {

    override fun setLightMode(activity: Activity, isFontDark: Boolean): Boolean {
        return super.setLightMode(activity, isFontDark)
    }
}