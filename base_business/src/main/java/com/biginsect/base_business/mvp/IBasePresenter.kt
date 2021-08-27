package com.biginsect.base_business.mvp

import com.biginsect.mvp.MvpPresenter

/**
 *@author biginsect
 *Created at 2021/8/26 20:19
 */
interface IBasePresenter<T: IBaseView>: MvpPresenter<T> {

    fun setLoginStatus(isLogin: Boolean)

    fun getLoginStatus(): Boolean

    fun setLoginAccount(username: String)

    fun getLoginAccount(): String

    fun setLoginPassword(pwd: String)

    fun getLoginPassword(): String

    fun setCookies(domain: String, cookie: String)

    fun getCookies(domain: String): String
}