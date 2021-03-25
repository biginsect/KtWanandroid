package com.biginsect.ktwanandroid

import com.biginsect.mvp.BaseMvpPresenter

/**
 *@author biginsect
 *Created at 2021/3/19 11:31
 */
class MainPresenter: BaseMvpPresenter<Contract.IMainView>(), Contract.IMainPresenter {

    override fun cal(a: Int, b: Int) {
        view.show(2+3)
        ifViewAttached {
        }
    }
}