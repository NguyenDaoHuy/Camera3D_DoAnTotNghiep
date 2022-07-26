package com.bhsoft.ar3d.ui.main

import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityMainBinding
import com.bhsoft.ar3d.ui.base.activity.BaseMVVMActivity
import com.bhsoft.ar3d.ui.utils.OpenFragmentUtils

class MainActivity : BaseMVVMActivity<MainCallBack, MainViewModel>(), MainCallBack {

    override fun getLayoutMain() = R.layout.activity_main

    override fun setEvents() {
    }

    override fun initComponents() {
        getBindingData().viewModel = mModel
        OpenFragmentUtils.openUserFragment(supportFragmentManager)
    }

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    override fun getBindingData() = mBinding as ActivityMainBinding

}