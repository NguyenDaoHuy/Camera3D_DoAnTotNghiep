package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.folder

import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.io.File
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

class FolderImageViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<FolderImageCallBack>(appDatabase, interactCommon, scheduler) {

    private var folderImageList : ArrayList<Pictures>? = ArrayList()

    fun getFolder(text: String){
        folderImageList!!.clear()
        val filePath = "/storage/emulated/0/DCIM/ObjectDetected"
        val file = File(filePath)
        val files = file.listFiles()
        if (files!=null){
            for (file1 : File in files){
               if(file1.name.toLowerCase().contains(text.toLowerCase())){
                   folderImageList!!.add(Pictures(file1.path,file1.name,file1.length()))
               }
            }
        }
    }
    fun getFileImageList():List<Pictures>{
        Collections.sort(folderImageList);
        return folderImageList!!
    }
}