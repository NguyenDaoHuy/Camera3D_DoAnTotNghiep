package com.vmodev.realmmvp.eventbus

import androidx.annotation.MainThread
import com.bhsoft.ar3d.common.eventbus.ActionBus
import com.bhsoft.ar3d.common.eventbus.ElementBus

//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************


@Suppress("UNCHECKED_CAST")
class Bus {
    private val elementBuses: MutableList<ElementBus> = mutableListOf()

    fun <T> register(idObjectPost: String, actionBus: ActionBus<T>) {
        for (elementBus in elementBuses) {
            if (elementBus.id.equals(idObjectPost)) {
                elementBus.listAction.add(actionBus)
                return
            }
        }
        val elementBus = ElementBus(idObjectPost)
        elementBus.listAction.add(actionBus)
        elementBuses.add(elementBus)
    }

    fun <T> registerList(idObjectPost: String, actionBus: ActionBus<MutableList<T>>) {
        val className = "list" + idObjectPost
        for (elementBus in elementBuses) {
            if (elementBus.id.equals(className)) {
                elementBus.listAction.add(actionBus)
                return
            }
        }
        val elementBus = ElementBus(className)
        elementBus.listAction.add(actionBus)
        elementBuses.add(elementBus)
    }

    fun <T> unregister(idObjectPost: String, actionBus: ActionBus<T>) {
        for (elementBus in elementBuses) {
            if (idObjectPost.equals(elementBus.id)) {
                for (action in elementBus.listAction) {
                    if (action == actionBus) {
                        elementBus.listAction.remove(actionBus)
                        break
                    }
                }
                break
            }
        }
    }

    fun <T> unregisterList(idObjectPost: String, actionBus: ActionBus<MutableList<T>>) {
        val className = "list" + idObjectPost
        for (elementBus in elementBuses) {
            if (className.equals(elementBus.id)) {
                for (action in elementBus.listAction) {
                    if (action == actionBus) {
                        elementBus.listAction.remove(actionBus)
                        break
                    }
                }
                break
            }
        }
    }


    @MainThread
    fun <T> post(idObjectPost: String, t: T) {
        for (elementBus in elementBuses) {
            if (elementBus.id.equals(idObjectPost)) {
                for (actionBus in elementBus.listAction) (actionBus as ActionBus<T>).call(t)
                break
            }
        }
    }

    @MainThread
    fun <T> postList(idObjectPost: String, list: MutableList<T>) {
        val clazzName = "list" + idObjectPost
        for (elementBus in elementBuses) {
            if (elementBus.id.equals(clazzName)) {
                for (actionBus in elementBus.listAction) (actionBus as ActionBus<MutableList<T>>).call(list)
                break
            }
        }
    }
}
