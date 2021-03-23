package com.biginsect.base_business.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.biginsect.base_business.R
import com.biginsect.base_business.widget.StateLayout
import com.biginsect.base_business.widget.TitleBar
import kotlinx.android.synthetic.main.activity_base.*
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragmentDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *@author biginsect
 *Created at 2021/2/24 19:47
 */
abstract class BaseFragment : Fragment(), StateLayout.OnReloadClickListener, ISupportFragment {

    protected lateinit var mRootView: View
    private val mDelegate: SupportFragmentDelegate by lazy { SupportFragmentDelegate(this) }
    protected lateinit var mAppActivity: AppCompatActivity

    private lateinit var mTitleBar: TitleBar
    private lateinit var mStateLayout: StateLayout

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
        mStateLayout.reloadClickListener = this

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTitleBar = tb_base
        mStateLayout = sl_base
    }

    override fun onReload() {

    }

    override fun switchStatus(state: StateLayout.State) {
        mStateLayout.visibility = View.VISIBLE
        mStateLayout.switchStatus(state)
    }

    override fun getSupportDelegate(): SupportFragmentDelegate {
        return mDelegate
    }

    override fun extraTransaction(): ExtraTransaction {
        return mDelegate.extraTransaction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDelegate.onAttach(context as AppCompatActivity)
        mAppActivity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mDelegate.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelegate.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDelegate.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mDelegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun enqueueAction(runnable: Runnable?) {
        mDelegate.enqueueAction(runnable)
    }

    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        mDelegate.onEnterAnimationEnd(savedInstanceState)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return mDelegate.onCreateFragmentAnimator()
    }

    override fun getFragmentAnimator(): FragmentAnimator {
        return mDelegate.fragmentAnimator
    }

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    override fun onBackPressedSupport(): Boolean {
        return mDelegate.onBackPressedSupport()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mDelegate.onLazyInitView(savedInstanceState)
    }

    override fun onSupportVisible() {
        mDelegate.onSupportVisible()
    }

    override fun onSupportInvisible() {
        mDelegate.onSupportInvisible()
    }

    final override fun isSupportVisible(): Boolean {
        return mDelegate.isSupportVisible
    }

    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        mDelegate.setFragmentResult(resultCode, bundle)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        mDelegate.onFragmentResult(requestCode, resultCode, data)
    }

    override fun onNewBundle(args: Bundle?) {
        mDelegate.onNewBundle(args)
    }

    override fun putNewBundle(newBundle: Bundle?) {
        mDelegate.putNewBundle(newBundle)
    }

    protected fun showAndHide(showFragment: ISupportFragment, hideFragment: ISupportFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    protected fun setEmptyText(@StringRes resId: Int) {
        this.setEmptyText(getString(resId))
    }

    protected fun setEmptyText(text: String) {
        mStateLayout.setEmptyText(text)
    }

    protected fun setTitle(titleText: String) {
        if (!TextUtils.isEmpty(titleText)) {
            mTitleBar.setTitle(titleText)
        }
    }

    protected fun setTitle(@StringRes resId: Int) {
        mTitleBar.setTitle(resId)
    }

    abstract fun getLayoutId(): Int
}