package com.biginsect.base_bussiness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

/**
 *@author biginsect
 *Created at 2021/2/24 19:47
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.activity_base, container, false)
        val contentView = mRootView.findViewById<FrameLayout>(R.id.fl_container)
        if (getLayoutId() != 0) {
            contentView.addView(inflater.inflate(getLayoutId(), container, false))
        }

        return mRootView
    }

    abstract fun getLayoutId(): Int
}