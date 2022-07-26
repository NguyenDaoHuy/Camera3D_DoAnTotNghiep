package com.bhsoft.ar3d.common.eventbus
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

interface ActionBus<Data> :BaseAction {
    fun call(data: Data)
}