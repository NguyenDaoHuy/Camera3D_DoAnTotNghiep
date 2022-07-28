package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<GalleryCallBack>(appDatabase, interactCommon, scheduler) {

    private var filesImageList : ArrayList<Pictures>? = ArrayList()

    fun getImages(){
        val filePath = "/storage/emulated/0/DCIM/ar3d"
        val file = File(filePath)
        val files = file.listFiles()
        if (files!=null){
            for (file1 :File in files){
                if (file1.path.endsWith(".png")||file1.path.endsWith(".jpg")){
                    filesImageList!!.add(Pictures(file1.path,file1.name,file1.length()))
                }
            }
        }
    }
//    fun getFilesImageList(): List<Image> {
//        return filesImageList!!.reversed()
//    }
    fun getFileImageList():List<Pictures>{
        return filesImageList!!
    }
}