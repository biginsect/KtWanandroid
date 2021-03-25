package com.biginsect.ktwanandroid

import com.biginsect.base_business.mvp.BaseMvpActivity
import com.biginsect.ktwanandroid.util.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<Contract.IMainView, Contract.IMainPresenter>(), Contract.IMainView {

    override fun createPresenter(): Contract.IMainPresenter {
        return MainPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun show(result: Int) {
        showToast("result is $result")
    }

    override fun initView() {
        super.initView()
        btn_cal.setOnClickListener {
            mPresenter?.cal(3, 2)
        }
    }

    override fun getStatusBarColor(): Int {
        return R.color.color_993366FF
    }
}