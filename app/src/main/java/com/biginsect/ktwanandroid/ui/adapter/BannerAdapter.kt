package com.biginsect.ktwanandroid.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.biginsect.ktwanandroid.R
import com.biginsect.ktwanandroid.app.GlideApp
import com.biginsect.ktwanandroid.bean.HomeBanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *@author biginsect
 *Created at 2021/5/14 17:47
 */
class BannerAdapter : BaseQuickAdapter<HomeBanner.Banner, BannerAdapter.ViewHolder>(R.layout.recycler_item_banner) {

    override fun convert(holder: ViewHolder, item: HomeBanner.Banner) {
        GlideApp.with(context)
            .load(item.imagePath)
            .into(holder.mIvBanner)
        holder.mTvTitle.text = item.title
    }

    class ViewHolder(itemView: View): BaseViewHolder(itemView){
        internal val mIvBanner = itemView.findViewById<ImageView>(R.id.iv_banner)
        internal val mTvTitle = itemView.findViewById<TextView>(R.id.tv_banner_title)
    }
}