package com.bhsoft.ar3d.ui.fragment.ar_object_fragment

import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.FragmentObjectBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel

class ObjectFragment : BaseMvvmFragment<ObjectCallBack,ObjectViewModel>(),ObjectCallBack {
    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_object
    }

    override fun setEvents() {

    }

    override fun initComponents() {
        getBindingData().objectViewModel = mModel
        mModel.uiEventLiveData.observe(this) {
            when (it) {
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()

            }
        }
    }

    override fun getBindingData() = mBinding as FragmentObjectBinding

    override fun getViewModel(): Class<ObjectViewModel> {
        return ObjectViewModel::class.java
    }
}