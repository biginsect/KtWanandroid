package com.biginsect.ktwanandroid.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.biginsect.base_business.util.ScreenUtils
import com.biginsect.ktwanandroid.BuildConfig
import com.biginsect.ktwanandroid.util.Preferences
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
        Preferences.init(this.applicationContext)
        if (BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}