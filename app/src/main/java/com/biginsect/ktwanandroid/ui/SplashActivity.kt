package com.biginsect.ktwanandroid.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.biginsect.ktwanandroid.constant.RouterPath

/**
 *@author biginsect
 *Created at 2021/4/25 17:29
 */
class SplashActivity : AppCompatActivity() {

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val mToHomeTask by lazy {
        Runnable {
            ARouter.getInstance().build(RouterPath.HOME).navigation()
            finish()
        }
    }
    private val delay = 500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler.removeCallbacks(mToHomeTask)
        mHandler.postDelayed(mToHomeTask, delay)
    }
}