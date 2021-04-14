package com.biginsect.ktwanandroid

import com.biginsect.mvp.MvpPresenter
import com.biginsect.mvp.MvpView

/**
 *@author biginsect
 *Created at 2021/3/19 11:24
 */
interface Contract {
    interface IMainView: MvpView {
        fun show(result: Int)
    }

    interface IMainPresenter: MvpPresenter<IMainView>{
        suspend fun cal(a: Int, b: Int)
    }
}