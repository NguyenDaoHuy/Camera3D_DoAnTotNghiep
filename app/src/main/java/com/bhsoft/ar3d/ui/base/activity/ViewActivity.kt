package com.bhsoft.ar3d.ui.base.activity

import android.os.Bundle
import android.view.View
import com.bhsoft.ar3d.ui.base.BaseViewUI
import com.bhsoft.ar3d.ui.base.fragment.BaseFragment
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

interface ViewActivity : BaseViewUI{

    fun onCreateControl(savedInstanceState: Bundle?)

    fun onDestroyControl()

    fun findFragmentByTag(tag: String): BaseFragment

    fun setViewRoot(viewRoot: View)

    fun onBackParent()

    fun onStartControl()

    fun onStopControl()
}