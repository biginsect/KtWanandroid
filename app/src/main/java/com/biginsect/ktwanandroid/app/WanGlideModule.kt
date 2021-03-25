package com.biginsect.ktwanandroid.app

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 *@author biginsect
 *Created at 2021/3/19 17:17
 */

@GlideModule
class WanGlideModule: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }
}