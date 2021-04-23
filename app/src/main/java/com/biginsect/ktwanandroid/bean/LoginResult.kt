package com.biginsect.ktwanandroid.bean

/**
 *@author biginsect
 *Created at 2021/3/26 17:07
 */
data class LoginResult(var errorCode: Int, var errorMsg: String, var data: Any) {

    private val codeSuccess = 0

    fun isSuccess(): Boolean {
        return errorCode == codeSuccess
    }
}
