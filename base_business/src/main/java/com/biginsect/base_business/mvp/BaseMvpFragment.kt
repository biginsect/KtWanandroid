package com.biginsect.base_business.mvp

import android.content.Context
import android.os.Bundle
import android.view.View
import com.biginsect.base_business.ui.BaseFragment
import com.biginsect.mvp.*

/**
 *@author biginsect
 *Created at 2021/3/18 20:25
 */
abstract class BaseMvpFragment<V : IBaseView, P : IBasePresenter<V>>
    : BaseFragment(), MvpDelegateCallback<V, P>, IBaseView {

    private val mvpDelegate: FragmentMvpDelegate<V, P> by lazy {
        FragmentMvpDelegateImpl(
            this, this,
            keepPresenterDuringScreenOrientationChange = true,
            keepPresenterOnBackStack = true
        )
    }
    protected var mPresenter: P? = null

    override fun getPresenter(): P? {
        return mPresenter
    }

    override fun setPresenter(presenter: P?) {
        this.mPresenter = presenter
    }

    @Suppress("UNCHECKED_CAST")
    override fun getMvpView(): V? {
        return this as V
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mvpDelegate.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mvpDelegate.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvpDelegate.onPause()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onStop()
    }

    override fun onStart() {
        super.onStart()
        mvpDelegate.onStart()
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mvpDelegate.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mvpDelegate.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mvpDelegate.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvpDelegate.onSaveInstanceState(outState)
    }
}