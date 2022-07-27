package com.bhsoft.ar3d.di.builder

import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment
import com.bhsoft.ar3d.ui.fragment.home_fragment.HomeFragment
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

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeCameraFragment(): CameraFragment
}