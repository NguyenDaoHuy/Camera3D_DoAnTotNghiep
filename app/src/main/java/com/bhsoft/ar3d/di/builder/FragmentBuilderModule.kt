package com.bhsoft.ar3d.di.builder

import com.bhsoft.ar3d.ui.main.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment
}