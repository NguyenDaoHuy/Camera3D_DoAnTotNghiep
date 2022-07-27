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
) : BaseViewModel<HomeCallBack>(appDatabase, interactCommon, scheduler)  {

    companion object{
        const val ON_CLICK_CAMERA = 1
        const val ON_CLICK_GALLERY = 2
    }
    fun onCLickCamera(){
        uiEventLiveData.value = ON_CLICK_CAMERA
    }
    fun onClickGallery(){
        uiEventLiveData.value = ON_CLICK_GALLERY
    }
}