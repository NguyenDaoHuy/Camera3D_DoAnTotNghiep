package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject


class DetailsGalleryViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
    ) :BaseViewModel<DetailsGalleryCallBack>(appDatabase,interactCommon,scheduler){

     companion object{
        const val ON_CLICK_DELETE = 1
     }
    fun onClickDelete(){
        uiEventLiveData.value = ON_CLICK_DELETE
    }
    fun clickDeleteImage(context: Context,paths : String){
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = MediaStore.Images.Media.DATA + "= ?"
        val selectionArgs = arrayOf(File(paths).absolutePath)
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(queryUri,projection,selection,selectionArgs,null)
        if (cursor!!.moveToFirst()){
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val deleteUri = ContentUris.withAppendedId(queryUri,id)
            try {
                contentResolver.delete(deleteUri,null,null)
                Toast.makeText(context,"Delete successfully !!!",Toast.LENGTH_SHORT).show()
            }catch (e : Exception){
                e.printStackTrace()
                Toast.makeText(context,"Delete failed !!!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}