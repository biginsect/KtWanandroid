package com.biginsect.ktwanandroid

import android.util.Log
import android.widget.Toast
import com.biginsect.base_business.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<Contract.IMainView, Contract.IMainPresenter>(), Contract.IMainView {

    override fun createPresenter(): Contract.IMainPresenter {
        Log.d("","sssss create presenter")
        return MainPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun show(result: Int) {
        Toast.makeText(this@MainActivity, "result is $result", Toast.LENGTH_SHORT).show()
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