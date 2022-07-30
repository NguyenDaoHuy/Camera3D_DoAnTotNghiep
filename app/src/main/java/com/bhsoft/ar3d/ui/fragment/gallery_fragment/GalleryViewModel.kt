package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.local.AppDatabase
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

    companion object{
        const val GET_DATA_IMAGE_SUCCESS = 1
    }
    private var filesImageList : ArrayList<Pictures>? = ArrayList()

    fun getImages(){
        filesImageList!!.clear()
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
        uiEventLiveData.value = GET_DATA_IMAGE_SUCCESS
    }

    fun onRenameFile(context: Context,position:Int){
        val alterDialog = AlertDialog.Builder(context)
        alterDialog.setTitle("Rename to")
        val editText = EditText(context)
        val path = getFileImageList()[position].path
        val file = File(path)
        var nameFile = file.name
        nameFile = nameFile.substring(0,nameFile.lastIndexOf("."))
        editText.setText(nameFile)
        alterDialog.setView(editText)
        editText.requestFocus()
        alterDialog.setPositiveButton("0K") { dialogInterface, i ->
            val onlyPath = file.parentFile.absolutePath
            var ext = file.absolutePath
            ext = ext.substring(ext.lastIndexOf("."))
            val newPath = onlyPath + "/" + editText.text.toString() + ext
            val newFile = File(newPath)
            val renameFile = file.renameTo(newFile)
            if (renameFile){
                val resolver = context!!.contentResolver
                resolver.delete(
                    MediaStore.Files.getContentUri("external"),
                    MediaStore.MediaColumns.DATA + "=?",arrayOf(file.absolutePath))
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = Uri.fromFile(newFile)
                context!!.sendBroadcast(intent)
                getImages()
            }

        }
        alterDialog.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface!!.dismiss()
        }
        alterDialog.show()
    }

    fun getDeleteImage(context: Context,paths : String){
        val dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_confirm)
        val txtTitle = dialog!!.findViewById<TextView>(R.id.tv_content)
        txtTitle.text = context.resources.getString(R.string.Title_confirm_dialog_delete_image)
        val window = dialog!!.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAtributes = window.attributes
        windowAtributes.gravity = Gravity.CENTER
        window.attributes = windowAtributes
        if (Gravity.CENTER == Gravity.CENTER) {
            dialog!!.setCancelable(true)
        } else {
            dialog!!.setCancelable(false)
        }
        val dialogOK = dialog!!.findViewById<Button>(R.id.btn_ok)
        val dialogCancel = dialog!!.findViewById<Button>(R.id.btn_cancel)
        dialogOK.setOnClickListener {
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
                    Toast.makeText(context,"Delete successfully !!!", Toast.LENGTH_SHORT).show()
                    //set lai data
                    getImages()
                }catch (e : Exception){
                    e.printStackTrace()
                    Toast.makeText(context,"Delete failed !!!", Toast.LENGTH_SHORT).show()
                }
            }
            dialog!!.dismiss()
        }
        dialogCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog.show()
    }
    fun getFileImageList():List<Pictures>{
        return filesImageList!!.reversed()!!
    }
}