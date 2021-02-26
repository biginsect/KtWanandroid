package com.biginsect.base_bussiness.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.biginsect.base_bussiness.R
import kotlinx.android.synthetic.main.v_title.view.*

/**
 *@author biginsect
 *Created at 2021/2/25 20:27
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.v_title, this)
        iv_back.setOnClickListener {
            (iv_back.context as Activity).finish()
        }
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    override fun setTitle(@StringRes resId: Int) {
        tv_title.setText(resId)
    }

    fun setTitleColor(@ColorInt resId: Int) {
        tv_title.setTextColor(resId)
    }
}