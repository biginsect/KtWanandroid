package com.biginsect.base_business.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.biginsect.base_business.R
import com.biginsect.base_business.util.StatusBarUtils
import com.biginsect.base_business.widget.StatusBarViewStub
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

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

    protected fun hideSoftKeyboard(view: View){
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    protected fun showSnackMessage(@StringRes resId: Int){
        showSnackMessage(getString(resId))
    }

    protected fun showSnackMessage(msg: String) {
        //隐藏虚拟按键栏 | 防止点击屏幕时,隐藏虚拟按键栏又弹了出来
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        val snackbar = Snackbar.make(window.decorView, msg, Snackbar.LENGTH_SHORT)
        val textView: TextView = snackbar.view.findViewById(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.setAction(getString(R.string.get_it)) {
            snackbar.dismiss()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }.show()
        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        })
    }
}