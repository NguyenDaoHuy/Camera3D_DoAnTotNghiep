package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

class GalleryImageCropViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
) : BaseViewModel<GalleryImageCropCallBack>(appDatabase, interactCommon, scheduler) {

    companion object{
        const val GET_DATA_IMAGE_SUCCESS = 1
    }
    private var filesImageList : ArrayList<Pictures>? = ArrayList()


    fun getImages(text: String,folder:String){
        filesImageList!!.clear()
        val filePath = "/storage/emulated/0/DCIM/ObjectDetected/"
        val file = File(filePath + folder)
        val files = file.listFiles()
        //sap xep theo thoi gian chup
        if (files!=null){
            Arrays.sort(files) { p0, p1 -> p1!!.lastModified().compareTo(p0!!.lastModified()) }
            for (file1 :File in files){
                if (file1.path.endsWith(".png")||file1.path.endsWith(".jpg")){
                    filesImageList!!.add(Pictures(file1.path,file1.name,file1.length()))
                }
            }
        }else{

        }
        uiEventLiveData.value = GET_DATA_IMAGE_SUCCESS
    }

    fun onRenameFile(context: Context,position:Int,folder:String){
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
                getImages("",folder)
            }

        }
        alterDialog.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface!!.dismiss()
        }
        alterDialog.show()
    }

    fun getDeleteImage(context: Context,paths : String,folder:String){
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
            var file = File(paths)
            if (file.isFile){
                file.delete()
                Toast.makeText(context,"Delete successfully !!!", Toast.LENGTH_SHORT).show()
            }
            getImages("",folder)
            dialog!!.dismiss()
        }
        dialogCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog.show()
    }
    fun getFileImageList():List<Pictures>{
        return filesImageList!!
    }
}