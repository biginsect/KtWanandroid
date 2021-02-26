package com.biginsect.base_bussiness.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout

/**
 *@author biginsect
 *Created at 2021/2/26 20:06
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mContentContainer: ViewGroup? = null
    private lateinit var mReload: Wrapper
    private lateinit var mLoading: Wrapper
    private lateinit var mEmpty: Wrapper
    private var mReloadClickListener: OnReloadClickListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mContentContainer == null && childCount > 0){
            mContentContainer = getChildAt(0) as ViewGroup;
        }
        inflate(context, )
    }

    inner class Wrapper(private val stub: ViewStub, inflateListener: ViewStub.OnInflateListener) {

        internal var mContainer: View? = null

        init {
            stub.setOnInflateListener(inflateListener)
        }

        protected fun setVisibility(visibility: Int) {
            if (visibility != VISIBLE) {
                mContainer?.visibility = visibility
            } else {
                mContainer = stub.inflate()
                mContainer?.visibility = visibility
                bringChildToFront(mContainer)
            }
        }
    }

    enum class State {
        LOADING,
        FAILED,
        SUCCESS,
        EMPTY
    }

    interface OnReloadClickListener {

        fun onReload()

        fun switchStatus(state: State)
    }
}