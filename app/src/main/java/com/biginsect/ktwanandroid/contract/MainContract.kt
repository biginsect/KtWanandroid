package com.biginsect.ktwanandroid.contract

import com.biginsect.base_business.mvp.IBasePresenter
import com.biginsect.base_business.mvp.IBaseView

/**
 *@author biginsect
 *Created at 2021/3/19 11:24
 */
interface MainContract {

    interface IMainView: IBaseView {
        fun show(result: Int)
    }

    interface IMainPresenter: IBasePresenter<IMainView>{
        suspend fun cal(a: Int, b: Int)
    }
}