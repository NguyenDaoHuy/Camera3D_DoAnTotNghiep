package com.bhsoft.ar3d.data.remote

import com.bhsoft.ar3d.common.Constants
import com.bhsoft.ar3d.common.eventbus.ActionBus
import com.vmodev.realmmvp.eventbus.Bus
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

class InteractCommon {
    private val apiUser : ApiUser?
    private val bus: Bus
    init {
        bus = Bus()
        apiUser = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(ApiUser::class.java)
    }

    fun <T> register(idObjectPost: String, action: ActionBus<T>) {
        bus.register(idObjectPost, action)
    }

    fun <T> registerList(idObjectPost: String, action: ActionBus<MutableList<T>>) {
        bus.registerList(idObjectPost, action)
    }

    fun <T> unregister(idObjectPost: String, action: ActionBus<T>) {
        bus.unregister(idObjectPost, action)
    }


    fun <T> unregisterList(idObjectPost: String, action: ActionBus<MutableList<T>>) {
        bus.unregisterList(idObjectPost, action)
    }

    fun <T> post(idObjectPost: String, t: T) {
        bus.post(idObjectPost, t)
    }

    fun <T> postList(idObjectPost: String, t: MutableList<T>) {
        bus.postList(idObjectPost, t)
    }
}