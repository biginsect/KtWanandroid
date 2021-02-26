package com.biginsect.mvp

/**
 *@author biginsect
 *Created at 2021/2/23 15:59
 */
interface MvpDelegateCallback<V : MvpView, P : MvpPresenter<V>> {

    fun createPresenter(): P?

    fun getPresenter(): P?

    fun setPresenter(presenter: P?)

    fun getMvpView(): V?
}