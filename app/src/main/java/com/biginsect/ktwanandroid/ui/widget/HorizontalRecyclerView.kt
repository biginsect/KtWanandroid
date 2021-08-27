package com.biginsect.ktwanandroid.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 *@author biginsect
 *Created at 2021/5/14 19:51
 */
class HorizontalRecyclerView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet?,
    defStyleAttr: Int = 0
) : RecyclerView(context, attr, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}