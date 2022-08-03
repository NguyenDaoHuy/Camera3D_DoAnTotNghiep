package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.graphics.*
import android.widget.ImageView
import android.widget.TextView
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.BoxLable
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.File
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
    private var imageLabeler: ImageLabeler? = null
    private var listBitmap:ArrayList<Bitmap>? = null

     companion object{
         const val ON_CLICK_DELETE = 1
         const val ON_CLICK_DETECT = 2
         const val ON_DELETE_SUCCESS = 3
         const val ON_CLICK_SHARE = 4
         const val ON_VISIBLE_BUTTON = 5
         const val ON_CLICK_CROP_IMAGE = 6
         const val ON_TOAST_BOXES_NULL = 7
     }
    init {
        //Multiple object detection in static images
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        objectDetector = ObjectDetection.getClient(options)

        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build()
        )
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
        val inputImage = InputImage.fromBitmap(bitmap,0)
        objectDetector!!.process(inputImage).addOnSuccessListener { detectedObjects ->
            if (detectedObjects.isNotEmpty()){
                val builder = StringBuilder()
                boxes = ArrayList()
                for (`object` in detectedObjects) {
                    for (label in `object`.labels) {
                        builder.append(label.text)
                            .append(" : ")
                            .append(label.confidence)
                            .append("\n")
                    }
                    if (!`object`.labels.isEmpty()) {
                        boxes!!.add(BoxLable(`object`.boundingBox,`object`.labels[0].text))
                    } else {
                        boxes!!.add(BoxLable(`object`.boundingBox,"Unknown"))
                    }
                }
                txtOutput.text = builder.toString()
                drawDetectionResult(boxes!!,bitmap!!)
                uiEventLiveData.value = ON_VISIBLE_BUTTON
            }else{
                txtOutput.text = "Could not detected"
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
    fun cropImage(bitmap: Bitmap?){
        if(boxes!!.size == 0){
            uiEventLiveData.value = ON_TOAST_BOXES_NULL
        }else{
            listBitmap = ArrayList()
            for(boxe in boxes!!){
                val croppedBitmap = Bitmap.createBitmap(bitmap!!,
                    boxe.rect.left,
                    boxe.rect.top,
                    boxe.rect.width(),
                    boxe.rect.height(), null, false)
                listBitmap!!.add(croppedBitmap)
            }
        }
    }
}