package com.biginsect.ktwanandroid.presenter

import com.biginsect.base_business.mvp.BasePresenter
import com.biginsect.ktwanandroid.R
import com.biginsect.ktwanandroid.app.WanApp
import com.biginsect.ktwanandroid.bean.LoginResult
import com.biginsect.ktwanandroid.contract.LoginContract
import com.biginsect.ktwanandroid.http.RetrofitHelper
import com.biginsect.ktwanandroid.ui.widget.BaseObserver
import com.biginsect.ktwanandroid.utils.RxUtils

/**
 *@author biginsect
 *Created at 2021/8/25 17:24
 */
class LoginPresenter : BasePresenter<LoginContract.ILoginView>(), LoginContract.ILoginPresenter {

    override fun login(username: String, pwd: String) {
        addSubscribe(
            RetrofitHelper.getService()
                .login(username, pwd)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<LoginResult>(
                    view,
                    WanApp.instance.getString(R.string.login_failed)
                ) {
                    override fun onNext(result: LoginResult) {
                        setLoginAccount(result.username)
                        setLoginPassword(result.password)
                        setLoginStatus(true)
                        view?.loginSuccess()
                    }
                })
        )
    }
}