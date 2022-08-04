package com.bhsoft.ar3d.ui.fragment.camera_fragment

import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<CameraCallBack>(appDatabase, interactCommon, scheduler) {
    companion object{
        const val ON_CLICK_GALLERY = 1
        const val ON_CLICK_AR_OBJECT = 2
        const val ON_CLICK_SHARE = 3
        const val ON_CLICK_TAKE_PHOTO = 4
        const val ON_CLICK_AUTO_CAMERA = 5

    }
    fun onCLickGallery(){
        uiEventLiveData.value = ON_CLICK_GALLERY
    }
    fun onCLickArObject(){
        uiEventLiveData.value = ON_CLICK_AR_OBJECT
    }
    fun onCLickShare(){
        uiEventLiveData.value = ON_CLICK_SHARE
    }
    fun onCLickTakePhoto(){
        uiEventLiveData.value = ON_CLICK_TAKE_PHOTO
    }
    fun onClickAutoCamera(){
        uiEventLiveData.value = ON_CLICK_AUTO_CAMERA
    }
}