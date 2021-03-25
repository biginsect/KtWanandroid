package com.biginsect.mvp

import android.util.ArrayMap
import com.orhanobut.logger.Logger

/**
 *@author biginsect
 *Created at 2021/2/22 21:09
 */
class ActivityScopedCache {

    private companion object {
        const val TAG = "ActivityScopedCache"
    }

    private val presenterMap = ArrayMap<String, PresenterHolder>()

    private fun logViewIdNull() {
        Logger.e("viewId is null")
    }

    private fun logPresenterNull() {
        Logger.e("presenter is null")
    }

    private fun logViewStateNull() {
        Logger.e("ViewState is null")
    }

    fun clear() {
        presenterMap.clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun <P> obtainPresenter(viewId: String): P {
        val holder = presenterMap[viewId]
        return holder?.presenter as P
    }

    @Suppress("UNCHECKED_CAST")
    fun <VS> obtainViewState(viewId: String): VS {
        val holder = presenterMap[viewId]
        return holder?.viewState as VS
    }

    fun putPresenter(viewId: String?, presenter: MvpPresenter<*>?) {
        when {
            viewId == null -> logViewIdNull()
            presenter == null -> logPresenterNull()
            else -> {
                var holder = presenterMap[viewId]
                if (holder == null) {
                    holder = PresenterHolder()
                    presenterMap[viewId] = holder
                }
                holder.presenter = presenter
            }
        }
    }

    fun putViewState(viewId: String?, viewState: Any?) {
        when {
            viewId == null -> logViewIdNull()
            viewState == null -> logViewStateNull()
            else -> {
                var holder = presenterMap[viewId]
                if (holder == null) {
                    holder = PresenterHolder()
                    presenterMap[viewId] = holder
                }
                holder.viewState = viewState
            }
        }
    }

    fun remove(viewId: String?) {
        if (viewId == null) {
            logViewIdNull()
        } else {
            presenterMap.remove(viewId)
        }
    }

    internal class PresenterHolder {
        var presenter: MvpPresenter<*>? = null
        internal var viewState: Any? = null
    }
}