package com.biginsect.base_bussiness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *@author biginsect
 *Created at 2021/2/24 19:46
 */
abstract class BaseActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract fun getLayoutId(): Int
}