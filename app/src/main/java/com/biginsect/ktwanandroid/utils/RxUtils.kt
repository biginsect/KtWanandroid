package com.biginsect.ktwanandroid.utils

import com.biginsect.ktwanandroid.bean.BaseResult
import com.biginsect.ktwanandroid.http.exception.OtherException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *@author biginsect
 *Created at 2021/8/26 17:27
 */
object RxUtils {

    fun <T> rxSchedulerHelper(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> handleResult(): ObservableTransformer<BaseResult<T>, T> {
        return ObservableTransformer<BaseResult<T>, T> { observable ->
            observable?.flatMap {
                if (it.isSuccess()) {
                    createData(it.data)
                } else {
                    Observable.error(OtherException())
                }
            }
        }
    }

    private fun <T> createData(t: T): Observable<T> {
        return Observable.create { emitter ->
            try {
                emitter?.onNext(t)
                emitter?.onComplete()
            } catch (e: Exception) {
                emitter?.onError(e)
            }
        }
    }
}