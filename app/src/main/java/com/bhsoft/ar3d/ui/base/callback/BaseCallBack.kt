package com.bhsoft.ar3d.ui.base.callback

import com.bhsoft.ar3d.ui.base.BaseViewUI
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

interface BaseCallBack : BaseViewUI {
    fun error(id: String, error: Throwable)
}