package com.biginsect.ktwanandroid.listener

import android.text.Editable
import android.text.TextWatcher

/**
 *@author biginsect
 *Created at 2021/4/15 20:00
 */
abstract class SimpleTextWatcher: TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}