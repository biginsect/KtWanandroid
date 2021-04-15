package com.biginsect.base_business.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.biginsect.base_business.R
import com.biginsect.base_business.util.StatusBarUtils
import com.biginsect.base_business.widget.StatusBarViewStub

/**
 * 适配了状态栏
 *@author biginsect
 *Created at 2021/3/18 16:42
 */
abstract class AbsActivity : AppCompatActivity() {

    private lateinit var mStatusBarViewStub: StatusBarViewStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.layout_root)
        initStatusBar()
        setImmersive()
    }

    override fun setContentView(layoutResID: Int) {
        val rl = findViewById<FrameLayout>(R.id.fl_container_root)
        if (rl == null) {
            super.setContentView(layoutResID)
        } else {
            layoutInflater.inflate(layoutResID, rl, true)
        }
    }

    override fun setContentView(view: View?) {
        val rl = findViewById<FrameLayout>(R.id.fl_container_root)
        if (rl == null) {
            super.setContentView(view)
        } else {
            rl.removeAllViews()
            rl.addView(view)
        }
    }

    /**
     * 沉浸式
     * */
    protected fun setImmersive() {
        actionBar?.hide()
        StatusBarUtils.statusBarLightMode(this, isStatusBarDarkStyle())
    }

    private fun initStatusBar() {
        mStatusBarViewStub = findViewById(R.id.sb_root)
        if (showStatusBar()) {
            mStatusBarViewStub.visibility = View.VISIBLE
            mStatusBarViewStub.setBackgroundColor(getColor(getStatusBarColor()))
        } else {
            mStatusBarViewStub.visibility = View.GONE
        }
    }


    open fun showStatusBar(): Boolean {
        return true
    }

    open fun isStatusBarDarkStyle(): Boolean {
        return true
    }

    @ColorRes
    open fun getStatusBarColor(): Int {
        return R.color.color_FFFFFF
    }

    protected fun setStatusBarColor(@ColorRes resId: Int) {
        mStatusBarViewStub.setBackgroundColor(getColor(resId))
    }
}