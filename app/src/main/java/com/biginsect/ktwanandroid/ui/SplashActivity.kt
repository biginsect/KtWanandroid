package com.biginsect.ktwanandroid.ui

import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.launcher.ARouter
import com.biginsect.base_business.ui.BaseActivity
import com.biginsect.ktwanandroid.constant.RouterPath

/**
 *@author biginsect
 *Created at 2021/4/25 17:29
 */
class SplashActivity : BaseActivity() {

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val mToHomeTask by lazy {
        Runnable {
            ARouter.getInstance().build(RouterPath.HOME).navigation()
            finish()
        }
    }
    private val delay = 500L

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initData() {
        super.initData()
        mHandler.removeCallbacks(mToHomeTask)
        mHandler.postDelayed(mToHomeTask, delay)
    }

    override fun showStatusBar(): Boolean {
        return false
    }
}