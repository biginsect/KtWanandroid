package com.biginsect.base_business.widget.banner

import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.Px

/**
 *@author biginsect
 *Created at 2021/6/4 14:43
 */
interface Indicator {

    fun initIndicatorCount(pageCount: Int, currentPage: Int)

    fun getView(): View

    fun getParams(): RelativeLayout.LayoutParams

    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    fun onPageSelected(position: Int)

    fun onPageScrollStateChanged(state: Int)
}