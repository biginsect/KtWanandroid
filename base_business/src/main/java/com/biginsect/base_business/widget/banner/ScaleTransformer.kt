package com.biginsect.base_business.widget.banner

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 *@author biginsect
 *Created at 2021/6/4 11:27
 */
class ScaleTransformer(minScale: Float) : ViewPager2.PageTransformer {

    private var mMinScale = 0.85f
    private val mDefaultCenter = 0.5f

    init {
        if (minScale > 0.0f) {
            mMinScale = minScale
        }
    }

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height
        page.pivotX = pageWidth.shr(1).toFloat()
        page.pivotY = pageHeight.shr(1).toFloat()
        when {
            position < -1 -> {
                page.scaleX = mMinScale
                page.scaleY = mMinScale
                page.pivotX = pageWidth.toFloat()
            }
            position < 1 -> {
                if (position < 0) {
                    val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                    page.pivotX = pageWidth * (mDefaultCenter + mDefaultCenter * -position)
                } else {
                    val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                    page.pivotX = pageWidth * ((1 - position) * mDefaultCenter)
                }
            }
            else -> {
                page.pivotX = 0f
                page.scaleX = mMinScale
                page.scaleY = mMinScale
            }
        }
    }
}