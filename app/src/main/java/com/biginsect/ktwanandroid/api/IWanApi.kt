package com.biginsect.ktwanandroid.api

import com.biginsect.ktwanandroid.bean.BaseResult
import com.biginsect.ktwanandroid.bean.LoginResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *@author biginsect
 *Created at 2021/3/26 14:40
 */
interface IWanApi {

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username")userName: String, @Field("password")password: String): Observable<BaseResult<LoginResult>>

    /**
     * @param code 验证码-公众号获取
     * */
    @POST("user/register")
    fun register(@Field("username")userName: String, @Field("password")password: String, @Field("repassword")code: String)

    @GET("user/logout/json")
    fun logout()
}