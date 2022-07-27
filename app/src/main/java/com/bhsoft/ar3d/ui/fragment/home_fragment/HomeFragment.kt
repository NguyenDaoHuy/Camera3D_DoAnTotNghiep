package com.bhsoft.ar3d.ui.fragment.home_fragment

import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.FragmentHome2Binding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment

class HomeFragment : BaseMvvmFragment<HomeCallBack,HomeViewModel>(),HomeCallBack {

    override fun initComponents() {
        getBindingData().homeViewMocel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                HomeViewModel.ON_CLICK_CAMERA -> goToCamera()
                HomeViewModel.ON_CLICK_GALLERY -> goToGallery()
            }
        }
    }

    private fun goToGallery() {

    }

    private fun goToCamera() {
         val cameraFragment = CameraFragment()
         val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.content,cameraFragment)
         fragmentTransaction.addToBackStack(CameraFragment.TAG)
         fragmentTransaction.commit()
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_home2
    }

    override fun setEvents() {

    }

    override fun getBindingData() = mBinding as FragmentHome2Binding

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }
}