package com.bhsoft.ar3d.ui.fragment.home_fragment

import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<HomeCallBack>(appDatabase, interactCommon, scheduler) {
    companion object{
        const val ON_CLICK_GALLERY = 1
        const val ON_CLICK_AR_OBJECT = 2
        const val ON_CLICK_SHARE = 3
        const val ON_CLICK_TAKE_PHOTO = 4
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
}