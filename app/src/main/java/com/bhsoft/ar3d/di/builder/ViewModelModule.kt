package com.bhsoft.ar3d.di.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bhsoft.ar3d.di.model.ViewModelFactory
import com.bhsoft.ar3d.di.model.ViewModelKey
import com.bhsoft.ar3d.ui.fragment.home_fragment.HomeViewModel
import com.bhsoft.ar3d.ui.main.MainViewModel
import com.bhsoft.ar3d.ui.main.user.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindsUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}