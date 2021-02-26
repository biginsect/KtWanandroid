package com.biginsect.mvp

import androidx.annotation.UiThread

/**
 *@author biginsect
 *Created at 2021/2/22 21:01
 */
interface MvpPresenter<V: MvpView> {

    @UiThread
    fun attachView(v: V?)

    @UiThread
    fun detachView()

    @UiThread
    fun destroy()
}