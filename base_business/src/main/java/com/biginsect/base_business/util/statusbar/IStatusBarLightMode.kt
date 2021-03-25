package com.biginsect.base_business.util.statusbar

import android.app.Activity

/**
 * 是否设置状态栏图标文字为深色
 *@author biginsect
 *Created at 2021/3/22 18:06
 */
interface IStatusBarLightMode {

    fun setLightMode(activity: Activity, isFontDark: Boolean): Boolean
}