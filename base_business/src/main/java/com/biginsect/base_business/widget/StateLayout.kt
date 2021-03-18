package com.biginsect.base_business.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import com.allen.library.SuperButton
import com.biginsect.base_business.R

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
    private lateinit var mRetry: Wrapper
    private lateinit var mLoading: Wrapper
    private lateinit var mEmpty: Wrapper
    var reloadClickListener: OnReloadClickListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mContentContainer == null && childCount > 0) {
            mContentContainer = getChildAt(0) as ViewGroup
        }
        inflate(context, R.layout.vs_empty, this)
        inflate(context, R.layout.vs_loading, this)
        inflate(context, R.layout.vs_retry, this)

        mRetry = Wrapper(findViewById(R.id.vs_retry), this::handleRetryInflated)
        mLoading = Wrapper(findViewById(R.id.vs_loading), null)
        mEmpty = Wrapper(findViewById(R.id.vs_empty), null)
    }

    fun switchStatus(state: State) {
        when (state) {
            State.FAILED -> {
                mRetry.setVisibility(VISIBLE)
                mLoading.setVisibility(GONE)
                mEmpty.setVisibility(GONE)
                this.visibility = VISIBLE
                mContentContainer?.visibility = GONE
            }

            State.LOADING -> {
                mLoading.setVisibility(VISIBLE)
                mRetry.setVisibility(GONE)
                mEmpty.setVisibility(GONE)
                this.visibility = VISIBLE
                mContentContainer?.visibility = GONE
            }

            State.EMPTY -> {
                mEmpty.setVisibility(VISIBLE)
                mRetry.setVisibility(GONE)
                mLoading.setVisibility(GONE)
                mContentContainer?.visibility = GONE
            }

            State.SUCCESS -> {
                mLoading.setVisibility(GONE)
                mRetry.setVisibility(GONE)
                mEmpty.setVisibility(GONE)
                mContentContainer?.visibility = VISIBLE
            }
        }
    }

    fun setRetryText(content: String, buttonText: String, isShowRetry: Boolean) {
        val container = mRetry.mContainer
        container?.findViewById<TextView>(R.id.tv_retry)?.text = content
        if (!TextUtils.isEmpty(buttonText)) {
            val button = container?.findViewById<SuperButton>(R.id.btn_retry)
            button?.text = buttonText
            button?.visibility = if (isShowRetry) VISIBLE else GONE
        }
    }

    fun setEmptyText(text: String) {
        mEmpty.mContainer?.findViewById<TextView>(R.id.tv_empty)?.text = text
    }

    fun getEmptyRoot(): View? {
        return mEmpty.mContainer?.findViewById(R.id.ll_empty_root)
    }

    fun getLoadingRoot(): View? {
        return mLoading.mContainer?.findViewById(R.id.fl_loading_root)
    }

    fun getRetryRoot(): View? {
        return mRetry.mContainer?.findViewById(R.id.ll_retry_root)
    }

    private fun handleRetryInflated(vs: ViewStub, view: View) {
        view.findViewById<SuperButton>(R.id.btn_retry).setOnClickListener {
            reloadClickListener?.onReload()
        }
    }

    inner class Wrapper(private val stub: ViewStub, inflateListener: ViewStub.OnInflateListener?) {

        internal var mContainer: View? = null

        init {
            stub.setOnInflateListener(inflateListener)
        }

        internal fun setVisibility(visibility: Int) {
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