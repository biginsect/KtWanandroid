package com.biginsect.ktwanandroid

import androidx.lifecycle.lifecycleScope
import com.biginsect.base_business.ui.BaseActivity
import com.biginsect.ktwanandroid.util.RetrofitHelper
import com.biginsect.ktwanandroid.util.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

/**
 *@author biginsect
 *Created at 2021/4/14 20:59
 */
class LoginActivity: BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        super.initView()
        btn_login.setOnClickListener {
            val username = et_username.text.toString()
            val pwd = et_pwd.text.toString()
            lifecycleScope.launch {
                val result = RetrofitHelper.WanService.login(username, pwd)
                if (result.errorCode == 0){
                    showToast("登录成功")
                }
            }
        }
    }

    override fun requestData() {
        super.requestData()
    }

    override fun showStatusBar(): Boolean {
        return true
    }

    override fun isStatusBarDarkStyle(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return R.color.color_03A9F4
    }
}