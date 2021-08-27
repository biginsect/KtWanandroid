package com.biginsect.ktwanandroid.bean

/**
 *@author biginsect
 *Created at 2021/3/26 17:07
 */
data class LoginResult(
    var username: String,
    var password: String,
    var icon: String,
    var type: Int,
    var collectIds: List<Int>
)
