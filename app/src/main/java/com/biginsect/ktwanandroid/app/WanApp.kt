package com.biginsect.ktwanandroid.app

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.biginsect.ktwanandroid.BuildConfig
import com.biginsect.base_business.util.Preferences
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlin.properties.Delegates

/**
 *@author biginsect
 *Created at 2021/3/19 17:14
 */
class WanApp : Application() {

    companion object {
        var context: Context by Delegates.notNull()
            private set
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
        Logger.addLogAdapter(AndroidLogAdapter())
        Preferences.init(this.applicationContext)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}