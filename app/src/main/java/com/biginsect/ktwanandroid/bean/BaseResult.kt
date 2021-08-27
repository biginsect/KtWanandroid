package com.biginsect.ktwanandroid.bean

import com.biginsect.ktwanandroid.constant.Constants

/**
 *@author biginsect
 *Created at 2021/5/14 17:11
 */
open class BaseResult<T> {

    var errorCode: Int = -1

    var errorMsg: String? = null

    var data: T? = null

    fun isSuccess(): Boolean {
        return errorCode == Constants.CODE_SUCCESS
    }
}
