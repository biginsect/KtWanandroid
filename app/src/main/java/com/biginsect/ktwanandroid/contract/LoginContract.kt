package com.biginsect.ktwanandroid.contract

import com.biginsect.base_business.mvp.IBasePresenter
import com.biginsect.base_business.mvp.IBaseView

/**
 *@author biginsect
 *Created at 2021/8/25 17:21
 */
interface LoginContract {

    interface ILoginView : IBaseView{

        fun loginSuccess()
    }

    interface ILoginPresenter : IBasePresenter<ILoginView>{

        fun login(username: String, pwd: String)
    }

}