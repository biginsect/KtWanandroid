package com.biginsect.base_business.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.biginsect.base_business.R
import com.biginsect.base_business.widget.StateLayout
import com.biginsect.base_business.widget.TitleBar
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *@author biginsect
 *Created at 2021/2/24 19:46
 */
abstract class BaseActivity : AbsActivity(), ISupportActivity,
    StateLayout.OnReloadClickListener {

    private val mDelegate by lazy { SupportActivityDelegate(this) }
    private lateinit var unbinder: Unbinder
    private lateinit var mRootContainer: FrameLayout

    @BindView(R.id.tb_base)
    lateinit var mTitleBar: TitleBar

    @BindView(R.id.sl_base)
    lateinit var mStateLayout: StateLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        val rootView = layoutInflater.inflate(R.layout.activity_base, null)
        val mRootContainer = rootView.findViewById<FrameLayout>(R.id.fl_container)
        if (getLayoutId() != 0) {
            mRootContainer.addView(layoutInflater.inflate(getLayoutId(), null))
        }
        super.setContentView(rootView)
        unbinder = ButterKnife.bind(this, rootView)
        setStatusBarColor(R.color.Main)
        mStateLayout.reloadClickListener = this
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View?) {
        mRootContainer.addView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
        mDelegate.onDestroy()
    }

    fun setTitle(title: String) {
        mTitleBar.setTitle(title)
    }

    override fun setTitle(@StringRes resId: Int) {
        mTitleBar.visibility = View.VISIBLE
        mTitleBar.setTitle(resId)
    }

    protected fun setEmptyText(@StringRes resId: Int) {
        this.setEmptyText(getString(resId))
    }

    protected fun setEmptyText(text: String) {
        mStateLayout.setEmptyText(text)
    }

    override fun onReload() {

    }

    override fun switchStatus(state: StateLayout.State) {
        mStateLayout.visibility = View.VISIBLE
        mStateLayout.switchStatus(state)
    }

    override fun extraTransaction(): ExtraTransaction {
        return mDelegate.extraTransaction()
    }

    override fun getSupportDelegate(): SupportActivityDelegate {
        return mDelegate
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    final override fun onBackPressed() {
        mDelegate.onBackPressed()
    }

    override fun onBackPressedSupport() {
        mDelegate.onBackPressedSupport()
    }

    override fun getFragmentAnimator(): FragmentAnimator {
        return mDelegate.fragmentAnimator
    }

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return mDelegate.onCreateFragmentAnimator()
    }

    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    fun showFragment(fragment: ISupportFragment) {
        mDelegate.showHideFragment(fragment)
    }

    fun showAndHideFragment(showFragment: ISupportFragment, hideFragment: ISupportFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    fun replace(toFragment: ISupportFragment, addToBackStack: Boolean) {
        mDelegate.replaceFragment(toFragment, addToBackStack)
    }

    fun <T : ISupportFragment> findFragment(fragmentClass: Class<T>): T {
        return SupportHelper.findFragment(supportFragmentManager, fragmentClass)
    }

    abstract fun getLayoutId(): Int
}