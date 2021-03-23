package com.biginsect.ktwanandroid

import android.app.Application
import com.biginsect.base_business.util.ScreenUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 *@author biginsect
 *Created at 2021/3/19 17:14
 */
class WanApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}