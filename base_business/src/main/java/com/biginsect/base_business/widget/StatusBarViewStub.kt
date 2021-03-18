package com.biginsect.base_business.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.biginsect.base_business.utils.ScreenUtils

/**
 * 适配水滴屏等异形机
 *@author biginsect
 *Created at 2021/3/18 15:18
 */
class StatusBarViewStub @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttrs: Int = 0
) : View(context, attributeSet, defStyleAttrs) {

    private var realHeight = 0

    init {
        realHeight = ScreenUtils.getScreenRealHeight(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.getSize(widthMeasureSpec), realHeight)
    }
}