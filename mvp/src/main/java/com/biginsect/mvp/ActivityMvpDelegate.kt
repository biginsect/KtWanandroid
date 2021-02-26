package com.biginsect.mvp

import android.os.Bundle

/**
 *@author biginsect
 *Created at 2021/2/22 21:03
 */
interface ActivityMvpDelegate<V : MvpView, P : MvpPresenter<V>> {

    fun onCreate(bundle: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun onRestart()

    fun onContentChange()

    fun onSaveInstanceState(outState: Bundle?)

    fun onPostCreate(bundle: Bundle?)
}