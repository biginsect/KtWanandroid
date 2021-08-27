package com.biginsect.ktwanandroid.ui.widget

import com.biginsect.base_business.mvp.IBaseView
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.observers.ResourceObserver
import retrofit2.HttpException

/**
 *@author biginsect
 *Created at 2021/8/25 17:46
 */
abstract class BaseObserver<T>(view: IBaseView?) : ResourceObserver<T>() {

    private var mErrorMsg: String? = null
    private var mView: IBaseView? = null

    init {
        mView = view
    }

    constructor(view: IBaseView?, errorMsg: String) : this(view) {
        mErrorMsg = errorMsg
    }

    override fun onComplete() {

    }

    abstract override fun onNext(result: T)

    override fun onError(e: Throwable?) {
        when {
            !mErrorMsg.isNullOrEmpty() -> mView?.showErrorMessage(mErrorMsg)
            e is HttpException -> mView?.showErrorMessage("network anomaly.")
            else -> {
                mView?.showErrorMessage("unknown error.")
                Logger.d(e?.toString())
            }
        }

    }
}