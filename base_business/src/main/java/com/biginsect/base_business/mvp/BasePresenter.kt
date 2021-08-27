package com.biginsect.base_business.mvp

import com.biginsect.base_business.constants.SPConstants
import com.biginsect.base_business.util.Preferences
import com.biginsect.mvp.BaseMvpPresenter

/**
 *@author biginsect
 *Created at 2021/8/26 20:24
 */
abstract class BasePresenter<T : IBaseView> : BaseMvpPresenter<T>(), IBasePresenter<T> {

    private var isLogin: Boolean by Preferences(SPConstants.LOGIN_STATUS, false)
    private var username by Preferences(SPConstants.LOGIN_STATUS, "")
    private var password by Preferences(SPConstants.PASSWORD, "")

    override fun setLoginStatus(isLogin: Boolean) {
        this.isLogin = isLogin
    }

    override fun getLoginStatus(): Boolean {
        return isLogin
    }

    override fun setLoginAccount(username: String) {
        this.username = username
    }

    override fun getLoginAccount(): String {
        return username
    }

    override fun setLoginPassword(pwd: String) {
        password = pwd
    }

    override fun getLoginPassword(): String {
        return password
    }

    override fun setCookies(domain: String, cookie: String) {
        TODO("Not yet implemented")
    }

    override fun getCookies(domain: String): String {
        TODO("Not yet implemented")
    }
}