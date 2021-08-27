package com.biginsect.ktwanandroid.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.biginsect.base_business.mvp.BaseMvpActivity
import com.biginsect.ktwanandroid.contract.MainContract
import com.biginsect.ktwanandroid.presenter.MainPresenter
import com.biginsect.ktwanandroid.R
import com.biginsect.ktwanandroid.constant.RouterPath
import com.biginsect.ktwanandroid.ui.adapter.BannerAdapter

@Route(path = RouterPath.HOME)
class MainActivity : BaseMvpActivity<MainContract.IMainView, MainContract.IMainPresenter>(),
    MainContract.IMainView {

    private lateinit var mBannerAdapter: BannerAdapter

    override fun createPresenter(): MainContract.IMainPresenter {
        return MainPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun show(result: Int) {

    }

    override fun initView() {
        super.initView()
        mBannerAdapter = BannerAdapter()
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