package com.biginsect.ktwanandroid

import androidx.lifecycle.lifecycleScope
import com.biginsect.base_business.ui.BaseActivity
import com.biginsect.ktwanandroid.listener.SimpleTextWatcher
import com.biginsect.ktwanandroid.http.RetrofitHelper
import com.biginsect.ktwanandroid.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

/**
 *@author biginsect
 *Created at 2021/4/14 20:59
 */
class LoginActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        super.initView()
        et_pwd.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til_pwd.isErrorEnabled = false
                til_pwd.error = ""
            }
        })
        et_username.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til_username.isErrorEnabled = false
                til_username.error = ""
            }
        })
        btn_login.setOnClickListener {
            checkAndLogin()
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

    private fun checkAndLogin() {
        hideSoftKeyboard(btn_login)
        val username = et_username.text.toString()
        val pwd = et_pwd.text.toString()

        when {
            username.isEmpty() -> {
                til_username.isErrorEnabled = true
                til_username.error = getString(R.string.no_empty)
            }
            pwd.isEmpty() -> {
                til_pwd.isErrorEnabled = true
                til_pwd.error = getString(R.string.no_empty)
            }
            else -> {
                til_username.isErrorEnabled = false
                til_pwd.isErrorEnabled = false
                realLogin(username, pwd)
            }
        }

    }

    private fun realLogin(username: String, pwd: String) {
        lifecycleScope.launch {
            val loginResult = RetrofitHelper.getService().login(username, pwd)
            if (loginResult.isSuccess()) {
                showToast(R.string.login_success)
            }
        }
    }
}