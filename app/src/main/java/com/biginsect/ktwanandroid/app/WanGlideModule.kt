package com.biginsect.ktwanandroid.app

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 *@author biginsect
 *Created at 2021/3/19 17:17
 */

@GlideModule
class WanGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(
            RequestOptions()
                .centerCrop()
                .error(ColorDrawable(Color.parseColor("#ededed")))
                .placeholder(ColorDrawable(Color.parseColor("#ededed")))
        )
    }
}