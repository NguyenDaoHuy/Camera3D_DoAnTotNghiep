package com.bhsoft.ar3d.di.model

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
