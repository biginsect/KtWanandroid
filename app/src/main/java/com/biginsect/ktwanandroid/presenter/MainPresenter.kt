package com.biginsect.ktwanandroid.presenter

import com.biginsect.base_business.mvp.BasePresenter
import com.biginsect.ktwanandroid.contract.MainContract

/**
 *@author biginsect
 *Created at 2021/3/19 11:31
 */
class MainPresenter: BasePresenter<MainContract.IMainView>(), MainContract.IMainPresenter {

    override suspend fun cal(a: Int, b: Int) {

    }

}