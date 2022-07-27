package com.bhsoft.ar3d.ui.fragment.camera_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : BaseMvvmFragment<CameraCallBack,CameraViewModel>(),CameraCallBack {

    private var imageCapture : ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExcutor:ExecutorService
    private lateinit var vibrator:Vibrator

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
        if(allPermissionGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(requireActivity(),Constants.REQUIRED_PERMISSIONS,Constants.REQUEST_CODE_PERMISSIONS)
        }
    }
    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
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
        outputDirectory = getOutputDirectory()
        onVibrator()
        takePhoto()
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
                    initToast("Photo Saved $savedUri")
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

    private fun goToArObject() {
        initToast("ArObject")
    }

    private fun goToGallery() {
        initToast("Gallery")
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