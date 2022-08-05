package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.BoxLable
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.Constants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.collections.ArrayList


class DetailsGalleryViewModel @Inject constructor(
    appDatabase: AppDatabase,
    interactCommon: InteractCommon,
    scheduler: Executor
    ) :BaseViewModel<DetailsGalleryCallBack>(appDatabase,interactCommon,scheduler){

    lateinit var imgInput : ImageView
    lateinit var txtOutput : TextView
    private var objectDetector : ObjectDetector?=null
    private var labeler  :ImageLabeler?=null
    private var boxes : MutableList<BoxLable>?=null
    private var inputImage : InputImage?=null
    private var listImageCroped:ArrayList<Image>? = null


     companion object{
         const val ON_CLICK_DELETE = 1
         const val ON_CLICK_DETECT = 2
         const val ON_DELETE_SUCCESS = 3
         const val ON_CLICK_SHARE = 4
         const val PROGRESS_DIALOG = 5
         const val PROGRESS_DIALOG_DISSMISS = 6
         const val ON_VISIBLE_BUTTON = 7
         const val ON_CLICK_CROP_IMAGE = 8
         const val ON_TOAST_BOXES_NULL = 9
         const val ON_SAVE_IMAGE_SUCCESS = 10
     }
    init {
        //Multiple object detection in static images
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        objectDetector = ObjectDetection.getClient(options)


        val option = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()
        labeler = ImageLabeling.getClient(option)
    }
    fun onClickDelete(){
        uiEventLiveData.value = ON_CLICK_DELETE
    }
    fun clickDeleteImage(paths : String){
        val file = File(paths)
        if(file.isFile){
            file.delete()
            uiEventLiveData.value = ON_DELETE_SUCCESS
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
        uiEventLiveData.value = PROGRESS_DIALOG
        inputImage = InputImage.fromBitmap(bitmap,0)
        listImageCroped = ArrayList()
        //detect
        objectDetector!!.process(inputImage).addOnSuccessListener { detectedObjects ->
            if (detectedObjects.isNotEmpty()){
                boxes = ArrayList()
                var i = 0
                var builder = StringBuilder()
                for (`object` in detectedObjects) {
                    val bounds = `object`.boundingBox
                    val croppedBitmap = Bitmap.createBitmap(bitmap!!,bounds.left,bounds.top,bounds.width(),bounds.height())
                    var inputImage2 = InputImage.fromBitmap(croppedBitmap,0)
                    //labeling
                    labeler!!.process(inputImage2).addOnSuccessListener { imageLabels ->
                        //dismiss progress_dialog
                        uiEventLiveData.value = PROGRESS_DIALOG_DISSMISS
                        i++
                        if (imageLabels.count()>0){
                            val maxValue =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    imageLabels.stream().max(Comparator.comparing { v -> v.confidence }).get()
                                } else {
                                    imageLabels[0]
                                }
                            for (labels in imageLabels){
                                if (labels.confidence == maxValue.confidence){
                                    builder.append(labels.text)
                                        .append(" : ")
                                        .append(labels.confidence)
                                        .append("\n")
                                    boxes!!.add(BoxLable(bounds,labels.text))
                                    listImageCroped!!.add(Image(labels.text,croppedBitmap))
                                }
                            }
                        }
                        else {
                            boxes!!.add(BoxLable(bounds,"Unknown"))
                            listImageCroped!!.add(Image("Unknown",croppedBitmap))
                        }
                        if (i == detectedObjects.size){
                            uiEventLiveData.value = ON_VISIBLE_BUTTON
                            txtOutput.text = builder.toString()
                            drawDetectionResult(boxes!!,bitmap!!)
                        }
                    }
                }
            }else{
                txtOutput.text = "Could not detected"
                uiEventLiveData.value = PROGRESS_DIALOG_DISSMISS
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }
    fun onClickShare(){
        uiEventLiveData.value = ON_CLICK_SHARE
    }
    fun onClickCropImage(){
        uiEventLiveData.value = ON_CLICK_CROP_IMAGE
    }
    fun getListImageCroped() : ArrayList<Image>{
        return listImageCroped!!
    }
    fun saveImageCropedToGallery(){
        for (image in listImageCroped!!){
            savePhoto(image)
        }
    }

    private fun savePhoto(image: Image) {
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory().toString() +"/"
                    + Environment.DIRECTORY_DCIM + "/ObjectDetected" + "/${image.name}/")
        if (!mediaStorageDir.isDirectory){
            mediaStorageDir.mkdirs()
        }

        var outputStream: FileOutputStream? = null
        val outFile = File(mediaStorageDir, image.name +"_"+ SimpleDateFormat(
            Constants.FILE_NAME_FORMAT,
            Locale.getDefault())
            .format(System.currentTimeMillis()) +".jpg")
        try {
            outputStream = FileOutputStream(outFile)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        image.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        try {
            outputStream!!.flush()
            outputStream!!.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        uiEventLiveData.value = ON_SAVE_IMAGE_SUCCESS
    }
}