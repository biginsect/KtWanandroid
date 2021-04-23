package com.biginsect.ktwanandroid

import com.biginsect.ktwanandroid.util.RetrofitHelper
import com.biginsect.mvp.BaseMvpPresenter

/**
 *@author biginsect
 *Created at 2021/3/19 11:31
 */
class MainPresenter: BaseMvpPresenter<Contract.IMainView>(), Contract.IMainPresenter {

    override suspend fun cal(a: Int, b: Int) {

    }

}