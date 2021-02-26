package com.biginsect.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import java.util.*

/**
 *@author biginsect
 *Created at 2021/2/23 16:02
 */
class ActivityMvpDelegateImpl<V : MvpView, P : MvpPresenter<V>>(
    delegateCallback: MvpDelegateCallback<V, P>?,
    activity: AppCompatActivity?,
    keepPresenterInstance: Boolean
) : ActivityMvpDelegate<V, P> {

    private companion object {
        var DEBUG = false
        const val KEY_VIEW_ID = "activity_mvp_id"
        const val TAG = "ActivityMvpDelegateImpl"
        fun retainPresenterInstance(
            keepPresenterInstance: Boolean,
            activity: AppCompatActivity
        ): Boolean {
            return keepPresenterInstance && (activity.isChangingConfigurations || activity.isFinishing)
        }
    }

    private lateinit var delegateCallback: MvpDelegateCallback<V, P>
    private var keepPresenterInstance = false
    private lateinit var activity: AppCompatActivity
    private var viewId: String? = null

    init {
        when {
            activity == null -> throw NullPointerException("activity is null")
            null == delegateCallback -> throw NullPointerException("delegateCallback is null")
            else -> {
                this.activity = activity
                this.delegateCallback = delegateCallback
                this.keepPresenterInstance = keepPresenterInstance
            }
        }
    }

    override fun onCreate(bundle: Bundle?) {
        var presenter: P? = null
        if (bundle != null && keepPresenterInstance) {
            viewId = bundle.getString(KEY_VIEW_ID)
            if (DEBUG) {
                Logger.d(TAG, "MosbyView ID = $viewId for MvpView ${delegateCallback.getMvpView()}")
            }

            if (viewId != null) {
                presenter = PresenterManager.getPresenter(activity, viewId)
                if (presenter != null) {
                    Logger.d(TAG, "Reused presenter $presenter for view ${delegateCallback.getMvpView()}")
                } else {
                    presenter = createViewAndPresenter()
                    if (DEBUG) {
                        Logger.d(
                            TAG, "No presenter found although view Id was here: $viewId ." +
                                    "Most likely this was caused by a process death. New Presenter created $presenter for view ${getMvpView()}"
                        )
                    }
                }
            }
        } else {
            presenter = createViewAndPresenter()
            if (DEBUG) {
                Logger.d(TAG, "New presenter $presenter for view ${getMvpView()}")
            }
        }
        if (presenter == null) {
            throw IllegalAccessException("Presenter is null. This seems to be a Mosby internal bug.")
        }
        delegateCallback.setPresenter(presenter)
        getPresenter().attachView(getMvpView())
        if (DEBUG) {
            Logger.d(TAG, "View ${getMvpView()} attached to presenter $presenter")
        }
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {
        val retainPresenterInstance = retainPresenterInstance(keepPresenterInstance, activity)
        getPresenter().detachView()

        if (!retainPresenterInstance) {
            getPresenter().destroy()
        }

        if (!retainPresenterInstance && viewId != null) {
            PresenterManager.remove(activity, viewId)
        }

        if (DEBUG) {
            if (retainPresenterInstance) {
                Logger.d(TAG, "View ${getMvpView()} destroy temporarily. View detached from presenter ${getPresenter()}")
            } else {
                Logger.d(TAG, "View ${getMvpView()} destroy permanently. View detached permanently from presenter ${getPresenter()}")
            }
        }
    }

    override fun onRestart() {

    }

    override fun onContentChange() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (keepPresenterInstance && outState != null) {
            outState.putString(KEY_VIEW_ID, viewId)
            if (DEBUG) {
                Logger.d(TAG, "Saving MosbyViewId into Bundle. ViewId: $viewId for view ${getMvpView()}")
            }
        }
    }

    override fun onPostCreate(bundle: Bundle?) {

    }

    private fun createViewAndPresenter(): P {
        val presenter = delegateCallback.createPresenter()
        if (presenter == null) {
            throw NullPointerException("Presenter returned from createPresenter is null. Activity is ${this.activity}")
        } else {
            if (this.keepPresenterInstance) {
                viewId = UUID.randomUUID().toString()
                PresenterManager.putPresenter(this.activity, this.viewId, presenter)
            }
        }
        return presenter
    }

    private fun getPresenter(): P {
        return delegateCallback.createPresenter()
            ?: throw NullPointerException("Presenter returned from getPresenter() is null")
    }

    private fun getMvpView(): V {
        return delegateCallback.getMvpView()
            ?: throw NullPointerException("View returned from getMvpView() is null")
    }
}