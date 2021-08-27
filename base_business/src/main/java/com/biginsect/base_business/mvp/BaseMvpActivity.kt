package com.biginsect.base_business.mvp

import android.os.Bundle
import com.biginsect.base_business.ui.BaseActivity
import com.biginsect.mvp.ActivityMvpDelegateImpl
import com.biginsect.mvp.MvpDelegateCallback

/**
 *@author biginsect
 *Created at 2021/3/18 20:08
 */
abstract class BaseMvpActivity<V : IBaseView, P : IBasePresenter<V>>
    : BaseActivity(), IBaseView, MvpDelegateCallback<V, P> {

    protected var mPresenter: P? = null
    private val mvpDelegate by lazy { ActivityMvpDelegateImpl(this, this, true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvpDelegate.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mvpDelegate.onPause()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onStop()
    }

    override fun onStart() {
        super.onStart()
        mvpDelegate.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        mvpDelegate.onRestart()
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.onResume()
    }

    override fun onContentChanged() {
        super.onContentChanged()
        mvpDelegate.onContentChange()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mvpDelegate.onPostCreate(savedInstanceState)
    }

    abstract override fun createPresenter(): P?

    override fun setPresenter(presenter: P?) {
        mPresenter = presenter
    }

    override fun getPresenter(): P? {
        return mPresenter
    }

    @Suppress("UNCHECKED_CAST")
    override fun getMvpView(): V? {
        return this as V
    }

    override fun showError() {

    }

    override fun showErrorMessage(errorMsg: String?) {
        if (!errorMsg.isNullOrEmpty()){
            showSnackMessage(errorMsg)
        }
    }
}