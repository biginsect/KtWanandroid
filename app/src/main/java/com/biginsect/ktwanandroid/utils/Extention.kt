package com.biginsect.ktwanandroid.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.biginsect.ktwanandroid.constant.Constants

/**
 *@author biginsect
 *Created at 2021/3/25 17:35
 */


fun Context.showToast(content: String) {
    Constants.toast?.apply {
        setText(content)
        show()
    } ?: run {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).apply {
            Constants.toast = this
        }.show()
    }
}

fun Context.showToast(@StringRes resId: Int) {
    showToast(getString(resId))
}

private const val SPLIT = ";"

fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies.map { cookie ->
        cookie.split(SPLIT.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }.forEach { it ->
        it.filterNot { set.contains(it) }.forEach { set.add(it) }
    }
    val iterator = set.iterator()
    while (iterator.hasNext()){
        val cookie = iterator.next()
        sb.append(cookie)
    }
    val last = sb.lastIndexOf(SPLIT)
    if (sb.length - 1 == last){
        sb.deleteCharAt(last)
    }

    return sb.toString()
}