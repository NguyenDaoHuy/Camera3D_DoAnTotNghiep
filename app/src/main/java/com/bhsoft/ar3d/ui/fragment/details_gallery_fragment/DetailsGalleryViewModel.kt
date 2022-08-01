package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.content.ContentUris
import android.content.Context
import android.graphics.*
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.BoxLable
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.File
import java.lang.StringBuilder
import java.util.concurrent.Executor
import javax.inject.Inject


class DetailsGalleryViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
    ) :BaseViewModel<DetailsGalleryCallBack>(appDatabase,interactCommon,scheduler){

    lateinit var imgInput : ImageView
    lateinit var txtOutput : TextView
    private var objectDetector : ObjectDetector?=null
    private var boxes : MutableList<BoxLable>?=null
     companion object{
        const val ON_CLICK_DELETE = 1
         const val ON_CLICK_DETECT = 2
     }
    init {
        //Multiple object detection in static images
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        objectDetector = ObjectDetection.getClient(options)
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

    fun clickDetectImage(){
        uiEventLiveData.value = ON_CLICK_DETECT
    }
   fun drawDetectionResult(listBox : MutableList<BoxLable>,bitmap: Bitmap){
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
        val canvas = Canvas(outputBitmap)
        val pentRect = Paint()
       // draw bounding box
        pentRect.color = Color.RED
        pentRect.style = Paint.Style.STROKE
        pentRect.strokeWidth = 6f

        val penLable = Paint()
       // calculate the right font size
        penLable.color = Color.YELLOW
        penLable.style = Paint.Style.FILL_AND_STROKE
        penLable.textSize = 85f
        penLable.strokeWidth = 1f
        for (boxWithLable : BoxLable in listBox){
            canvas.drawRect(boxWithLable.rect,pentRect)
            //Rect
            val lableSize = Rect(0,0,0,0)
            penLable.getTextBounds(boxWithLable.lable,0,boxWithLable.lable.length,lableSize)
            val fontSize = penLable.textSize*boxWithLable.rect.width()/lableSize.width()
            if (fontSize < penLable.textSize){
                penLable.textSize = fontSize
            }
            canvas.drawText(boxWithLable.lable,
                boxWithLable.rect.left.toFloat(), boxWithLable.rect.top + lableSize.height().toFloat(),penLable)
        }
       imgInput.setImageBitmap(outputBitmap)
    }
    fun runClassfication(bitmap: Bitmap?) {
        val inputImage = InputImage.fromBitmap(bitmap,0)
        objectDetector!!.process(inputImage).addOnSuccessListener { detectedObjects ->
            if (detectedObjects.isNotEmpty()){
                val builder = StringBuilder()
                boxes = ArrayList()
                for (objects: DetectedObject in detectedObjects){
                    if (objects.labels.isNotEmpty()){
                        val lable = objects.labels[0].text
                        builder.append(lable)
                            .append(": ")
                            .append(objects.labels[0].confidence).append("\n")
                        boxes!!.add(BoxLable(objects.boundingBox,lable))
                    }else{
                        builder.append("Unknow").append("\n")
                    }
                }
                txtOutput.text = builder.toString()
                drawDetectionResult(boxes!!,bitmap!!)
            }else{
                txtOutput.text = "Could not detected"
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }
}