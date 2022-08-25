package com.bhsoft.ar3d.ui.fragment.camera_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.MODEL
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.FragmentHomeBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryFragment
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.Camera_Detect_Activity
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : BaseMvvmFragment<CameraCallBack,CameraViewModel>(),CameraCallBack {

    private var imageCapture : ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExcutor:ExecutorService
    private lateinit var vibrator:Vibrator
    private var timer : Timer?=null
    private var checkAuto = false
    private var objectDetector : ObjectDetector?=null

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initComponents() {
        getBindingData().cameraViewModel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                CameraViewModel.ON_CLICK_GALLERY -> goToGallery()
                CameraViewModel.ON_CLICK_AR_OBJECT -> goToArObject()
                CameraViewModel.ON_CLICK_SHARE -> goToShare()
                CameraViewModel.ON_CLICK_TAKE_PHOTO -> onClickTakePhoto()
            }
        }
        checkPermissionGranted()
        cameraExcutor = Executors.newSingleThreadExecutor()
        vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        onClickAutoCamera()

        val localModel = LocalModel.Builder()
            .setAssetFilePath(MODEL)
            .build()
        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(10)
                .build()

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)
    }

    //Hiệu ứng rung khi ấn nút chụp ảnh
    private fun onVibrator(){
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }
    //check Permission Camera
    private fun checkPermissionGranted(){
        if(checkPermission()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(requireActivity(),Constants.REQUIRED_PERMISSIONS,Constants.REQUEST_CODE_PERMISSIONS)
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(checkPermission()){
                startCamera()
            }else{
                initToast("Permissions not granted by the user.")
                finishActivity()
            }
        }
    }
    // Camera bắt đầu hoạt động
    private fun startCamera(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                         getBindingData().viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }catch(e:Exception){
                Log.d(Constants.TAG,"start Camera Fail: ", e)
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }

    // Click nút chụp ảnh
    private fun onClickTakePhoto(){
        if(checkAuto){
            if (timer!=null){
                checkAuto = false
                timer!!.cancel()
                timer =null
                getBindingData().imgStop.visibility = View.GONE
                getBindingData().imgCamera.visibility = View.VISIBLE
                initToast("Diss Auto")
            }
        }
        outputDirectory = getOutputDirectory()
        onVibrator()
        takePhoto()
    }
    //Click nut chup anh lien tuc
    private  fun onClickAutoCamera() {
        getBindingData().clickCamera.setOnLongClickListener {
            onVibrator()
            getBindingData().imgStop.visibility = View.VISIBLE
            getBindingData().imgCamera.visibility = View.GONE
            if (!checkAuto){
                checkAuto = true
                timer = Timer()
                // A new thread with a 0,3-second delay before changing
                timer!!.scheduleAtFixedRate(
                    object : TimerTask() {
                        override fun run() {
                            outputDirectory = getOutputDirectory()
                            takePhoto()
                        }
                    },0, 300)
            }
            true
        }
    }
    @SuppressLint("NewApi")
    private fun getOutputDirectory() : File{
        val mediaStorageDir = File(getExternalStorageDirectory().toString() +"/"
                +Environment.DIRECTORY_DCIM + "/${resources.getString(R.string.app_name)}/")
        if (!mediaStorageDir.isDirectory){
            mediaStorageDir.mkdirs()
        }
       return mediaStorageDir
    }

    //Chụp ảnh và lưu
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
           outputDirectory, SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault())
                .format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }

            }
        )
    }

    private fun goToShare() {
        initToast("Share")
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun goToArObject() {
        initToast("ArObject")
        activity!!.startActivity(Intent(requireContext(), Camera_Detect_Activity::class.java))
    }

    private fun goToGallery() {
        val galleryFragment = GalleryFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content,galleryFragment)
        fragmentTransaction.addToBackStack(GalleryFragment.TAG)
        fragmentTransaction.commit()
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_home
    }

    override fun setEvents() {
    }

    override fun getBindingData() = mBinding as FragmentHomeBinding

    override fun getViewModel(): Class<CameraViewModel> {
        return CameraViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
       showMessage(error.message!!)
    }
    fun initToast(string : String){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExcutor.isShutdown
    }
    companion object{
        val TAG = CameraFragment::class.java.name
    }
}