package com.bhsoft.ar3d.di.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bhsoft.ar3d.di.model.ViewModelFactory
import com.bhsoft.ar3d.di.model.ViewModelKey
import com.bhsoft.ar3d.ui.fragment.ar_object_fragment.ObjectViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraViewModel
import com.bhsoft.ar3d.ui.fragment.details_gallery_fragment.DetailsGalleryViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.folder.FolderImageViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop.GalleryImageCropViewModel
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
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindsCameraViewModel(cameraViewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindsGalleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsGalleryViewModel::class)
    abstract fun bindsDetailsGalleryViewModel(detailsGalleryViewModel: DetailsGalleryViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryImageCropViewModel::class)
    abstract fun bindsGalleryImageCropViewModel(galleryImageCropViewModel: GalleryImageCropViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FolderImageViewModel::class)
    abstract fun bindsFolderImageViewModel(folderImageViewModel: FolderImageViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ObjectViewModel::class)
    abstract fun bindsObjectViewModel(objectViewModel: ObjectViewModel) : ViewModel

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}