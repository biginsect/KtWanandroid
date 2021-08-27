package com.biginsect.base_business.mvp

import com.biginsect.mvp.MvpView

/**
 *@author biginsect
 *Created at 2021/8/25 20:04
 */
interface IBaseView: MvpView {

    fun showError()

    fun showErrorMessage(errorMsg: String?)
}