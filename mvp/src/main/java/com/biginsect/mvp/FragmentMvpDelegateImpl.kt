package com.biginsect.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger
import java.lang.NullPointerException
import java.util.*

/**
 *@author biginsect
 *Created at 2021/2/23 19:36
 */
class FragmentMvpDelegateImpl<V : MvpView, P : MvpPresenter<V>>(
    fragment: Fragment?,
    delegateCallback: MvpDelegateCallback<V, P>?,
    keepPresenterDuringScreenOrientationChange: Boolean,
    keepPresenterOnBackStack: Boolean
) : FragmentMvpDelegate<V, P> {

    private companion object {
        const val KEY_VIEW_ID = "fragment.mvp.id"
        var DEBUG = true
        const val TAG = "FragmentMvpDelegateImpl"

        fun retainPresenterInstance(
            activity: Activity,
            fragment: Fragment,
            keepPresenterDuringScreenOrientationChange: Boolean,
            keepPresenterOnBackStack: Boolean
        ): Boolean {
            if (activity.isChangingConfigurations) {
                return keepPresenterDuringScreenOrientationChange
            }
            if (activity.isFinishing) {
                return false
            }
            if (keepPresenterOnBackStack) {
                return true
            }

            return !fragment.isRemoving
        }
    }

    private var viewId: String? = null
    private var fragment: Fragment
    private var keepPresenterOnBackStack = false
    private var keepPresenterDuringScreenOrientationChange = false
    private var delegateCallback: MvpDelegateCallback<V, P>
    private var onViewCreatedCalled = false

    init {
        when {
            delegateCallback == null -> throw NullPointerException("MvpDelegateCallback is null!")
            fragment == null -> throw NullPointerException("fragment is null")
            !keepPresenterDuringScreenOrientationChange && keepPresenterOnBackStack -> throw IllegalArgumentException(
                "It is not possible to keep the presenter on backstack, " +
                        "but NOT keep presenter through screen orientation changes. Keep presenter on backstack also requires keep presenter through screen orientation changes to be enabled"
            )
            else -> {
                this.fragment = fragment
                this.delegateCallback = delegateCallback
                this.keepPresenterDuringScreenOrientationChange = keepPresenterDuringScreenOrientationChange
                this.keepPresenterOnBackStack = keepPresenterOnBackStack
            }

        }
    }

    override fun onCreate(savedState: Bundle?) {
        var presenter: P? = null
        if (savedState != null && keepPresenterDuringScreenOrientationChange) {
            viewId = savedState.getString(KEY_VIEW_ID)
            if (DEBUG) {
                Logger.d("MosbyView ID $viewId for MvpView ${delegateCallback.getMvpView()}")
            }
            if (viewId != null) {
                presenter = PresenterManager.getPresenter(getActivity(), viewId)
                if (presenter != null) {
                    Logger.d("Reused presenter $presenter for view ${delegateCallback?.getMvpView()}")
                } else {
                    presenter = createViewIdAndPresenter()
                    if (DEBUG) {
                        Logger.d("No presenter found although view Id was here: $viewId ." +
                                    "Most likely this was caused by a process death. New Presenter created $presenter for view ${getMvpView()}"
                        )
                    }
                }
            }
        } else {
            presenter = createViewIdAndPresenter()
            if (DEBUG) {
                Logger.d("New presenter $presenter for view ${getMvpView()}")
            }
        }
        if (presenter == null) {
            throw IllegalStateException("Presenter is null. This seems to be a Mosby internal bug.")
        }

        delegateCallback.setPresenter(presenter)
    }

    override fun onDestroy() {
        val activity = getActivity()
        val retainPresenterInstance = retainPresenterInstance(activity, fragment, keepPresenterDuringScreenOrientationChange, keepPresenterOnBackStack)
        val presenter = getPresenter()
        if (!retainPresenterInstance) {
            presenter.destroy()
            Logger.d("Presenter destroyed. MvpView ${delegateCallback.getMvpView()} presenter: $presenter")
            if (viewId != null) {
                PresenterManager.remove(activity, viewId)
            }
        }
    }

    override fun onViewCreated(view: View, savedState: Bundle?) {
        val presenter = getPresenter()
        presenter.attachView(getMvpView())
        onViewCreatedCalled = true
        if (DEBUG) {
            Logger.d("View ${getMvpView()} attached to presenter $presenter")
        }
    }

    override fun onDestroyView() {
        onViewCreatedCalled = false
        getPresenter().detachView()
        if (DEBUG) {
            Logger.d("detached MvpView from Presenter. MvpView ${delegateCallback?.getMvpView()} presenter: ${getPresenter()}")
        }
    }

    override fun onPause() {

    }

    override fun onResume() {

    }

    override fun onStart() {
        if (!onViewCreatedCalled) {
            throw IllegalStateException("It seems that you are using ${delegateCallback.javaClass?.canonicalName}  as headless (UI less) fragment (because onViewCreated() has not been called or maybe delegation misses that part). Having a Presenter without a View (UI) doesn't make sense. Simply use an usual fragment instead of an MvpFragment if you want to use a UI less Fragment")
        }
    }

    override fun onStop() {

    }

    override fun onActivityCreated(saveInstanceState: Bundle?) {

    }

    override fun onAttach(context: Context) {

    }

    override fun onDetach() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState != null && (keepPresenterDuringScreenOrientationChange || keepPresenterOnBackStack)) {
            outState.putString(KEY_VIEW_ID, viewId)
            if (DEBUG) {
                Logger.d("Saving MosbyViewId into Bundle. ViewId: $viewId")
            }
        }
    }

    private fun createViewIdAndPresenter(): P {
        val presenter = this.delegateCallback.createPresenter()
        if (null == presenter) {
            throw NullPointerException("Presenter returned from createPresenter() is null. Activity is " + this.getActivity())
        } else {
            if (keepPresenterDuringScreenOrientationChange) {
                viewId = UUID.randomUUID().toString()
                PresenterManager.putPresenter(getActivity(), viewId, presenter)
            }
        }

        return presenter
    }

    private fun getActivity(): Activity {
        return fragment.activity ?: throw NullPointerException("Activity returned by Fragment.getActivity() is null. Fragment is $fragment")
    }

    private fun getMvpView(): V {
        return delegateCallback.getMvpView() ?: throw NullPointerException("View returned from getMvpView() is null")
    }

    private fun getPresenter(): P {
        return delegateCallback.getPresenter() ?: throw NullPointerException("Presenter returned from getPresenter() is null")
    }
}