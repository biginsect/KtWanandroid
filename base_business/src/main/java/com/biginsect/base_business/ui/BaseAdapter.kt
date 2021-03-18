package com.biginsect.base_business.ui

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *@author biginsect
 *Created at 2021/3/18 17:37
 */
abstract class BaseAdapter<D, VH : BaseViewHolder> :
    BaseQuickAdapter<D, VH> {

    constructor(@LayoutRes layoutId: Int, data: MutableList<D>) : super(layoutId, data)

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        headerLayout?.isHapticFeedbackEnabled = false
        return super.onCreateViewHolder(parent, viewType)
    }
}