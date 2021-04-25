package com.biginsect.ktwanandroid.ui

import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.biginsect.base_business.mvp.BaseMvpActivity
import com.biginsect.ktwanandroid.Contract
import com.biginsect.ktwanandroid.MainPresenter
import com.biginsect.ktwanandroid.R
import com.biginsect.ktwanandroid.constant.RouterPath
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

@Route(path = RouterPath.HOME)
class MainActivity : BaseMvpActivity<Contract.IMainView, Contract.IMainPresenter>(),
    Contract.IMainView {

    override fun createPresenter(): Contract.IMainPresenter {
        return MainPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun show(result: Int) {

    }

    override fun initView() {
        super.initView()
        btn_cal.setOnClickListener {
            lifecycleScope.launch {
                mPresenter?.cal(2, 3)
            }
        }
    }

    override fun getStatusBarColor(): Int {
        return R.color.color_FFFFFF
    }

    override fun isStatusBarDarkStyle(): Boolean {
        return true
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }
}