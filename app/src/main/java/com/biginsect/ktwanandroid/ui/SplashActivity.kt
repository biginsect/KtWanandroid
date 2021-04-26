package com.biginsect.ktwanandroid.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
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
        setStatusBarTranslucent()
        mHandler.removeCallbacks(mToHomeTask)
        mHandler.postDelayed(mToHomeTask, delay)
    }

    private fun setStatusBarTranslucent(){
        window.navigationBarColor = Color.TRANSPARENT
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }
}