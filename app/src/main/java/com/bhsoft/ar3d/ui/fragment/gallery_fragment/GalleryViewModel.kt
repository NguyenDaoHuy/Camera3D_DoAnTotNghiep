package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<GalleryCallBack>(appDatabase, interactCommon, scheduler) {

    private var filesImageList : ArrayList<Image>? = ArrayList()
    private var image : Image? = null

    @SuppressLint("Range")
    fun getDataImageFromDevices(folderName: String?, view: Context){
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.DATA + " like?"
        val selectionArg = arrayOf("%$folderName%")
        val cursor = view!!.contentResolver.query(uri, null, selection, selectionArg, null)
        if (cursor != null && cursor.moveToNext()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                val size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                val duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                image = Image(id, title, displayName, size, duration, path, dateAdded)
                filesImageList!!.add(image!!)
            } while (cursor.moveToNext())
        }
    }

    fun getFilesImageList(): List<Image> {
        return filesImageList!!.reversed()
    }
}