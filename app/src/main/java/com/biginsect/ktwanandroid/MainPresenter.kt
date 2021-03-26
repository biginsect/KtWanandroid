package com.biginsect.ktwanandroid

import com.biginsect.ktwanandroid.bean.LoginResponse
import com.biginsect.ktwanandroid.util.RetrofitHelper
import com.biginsect.mvp.BaseMvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *@author biginsect
 *Created at 2021/3/19 11:31
 */
class MainPresenter: BaseMvpPresenter<Contract.IMainView>(), Contract.IMainPresenter {

    override fun cal(a: Int, b: Int) {
        RetrofitHelper.WanService.login("", "").enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    if (response.body()?.errorCode == 0){
                        view?.show(200)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }
        })
    }
}