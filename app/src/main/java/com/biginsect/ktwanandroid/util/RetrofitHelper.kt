package com.biginsect.ktwanandroid.util

import android.util.Log
import com.biginsect.ktwanandroid.api.IWanApi
import com.biginsect.ktwanandroid.constant.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *@author biginsect
 *Created at 2021/3/25 18:03
 */
object RetrofitHelper {

    private const val TAG = "Retrofit"
    private const val LOG_PRE = "Http:"
    private const val KEY_USER_LOGIN = "user/login"
    private const val KEY_USER_REGISTER = "user/register"
    private const val KEY_COOKIE = "set-cookie"
    private const val NAME_COOKIE = "Cookie"
    private const val TIMEOUT_CONNECT = 30L
    private const val TIMEOUT_READ = 10L

    val WanService by lazy { getService(Constants.BASE_URL, IWanApi::class.java) }

    private fun <T> getService(url: String, service: Class<T>): T = create(url).create(service)

    private fun create(url: String): Retrofit {
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            //cookie for login
            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                val requestUrl = request.url.toString()
                val domain = request.url.host
                val containLoginOrRegister =
                    url.contains(KEY_USER_LOGIN) || url.contains(KEY_USER_REGISTER)
                if (containLoginOrRegister && response.headers(KEY_COOKIE).isNotEmpty()) {
                    val cookies = response.headers(KEY_COOKIE)
                    val cookie = encodeCookie(cookies)
                    saveCookie(requestUrl, domain, cookie)
                }

                response
            }
            //request cookie
            addInterceptor {
                val request = it.request()
                val urlBuilder = request.newBuilder()
                val host = request.url.host
                if (host.isNotEmpty()) {
                    val spHost by Preferences(host, "")
                    val cookie = if (spHost.isNotEmpty()) {
                        spHost
                    } else {
                        ""
                    }
                    if (cookie.isNotEmpty()) {
                        urlBuilder.addHeader(NAME_COOKIE, cookie)
                    }
                }
                it.proceed(request)
            }
            //log
            addInterceptor(HttpLoggingInterceptor {
                Log.d(TAG, LOG_PRE + it)
            }.apply { level = HttpLoggingInterceptor.Level.BODY })
        }

        return Retrofit.Builder().apply {
            baseUrl(url)
            client(okHttpClientBuilder.build())
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build()
    }

    private fun saveCookie(url: String?, host: String?, cookie: String) {
        url ?: return
        var spPath by Preferences(url, cookie)
        spPath = cookie
        host ?: return
        var spHost by Preferences(host, cookie)
        spHost = cookie
    }
}