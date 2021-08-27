package com.biginsect.ktwanandroid.ui.activity

import com.biginsect.base_business.mvp.BaseMvpActivity
import com.biginsect.ktwanandroid.R
import com.biginsect.ktwanandroid.contract.LoginContract
import com.biginsect.ktwanandroid.listener.SimpleTextWatcher
import com.biginsect.ktwanandroid.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 *@author biginsect
 *Created at 2021/4/14 20:59
 */
class LoginActivity : BaseMvpActivity<LoginContract.ILoginView, LoginContract.ILoginPresenter>(), LoginContract.ILoginView {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun createPresenter(): LoginContract.ILoginPresenter? {
        return LoginPresenter()
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
                mPresenter?.login(username, pwd)
            }
        }

    }

    override fun loginSuccess() {
        showSnackMessage(R.string.login_success)
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