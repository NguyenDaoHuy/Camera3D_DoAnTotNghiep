package com.bhsoft.ar3d.ui.fragment.ar_object_fragment

import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

class ObjectViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<ObjectCallBack>(appDatabase,interactCommon,scheduler){
}