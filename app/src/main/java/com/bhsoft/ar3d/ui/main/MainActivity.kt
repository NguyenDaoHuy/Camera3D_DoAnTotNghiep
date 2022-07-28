package com.bhsoft.ar3d.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhsoft.ar3d.BuildConfig
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityMainBinding
import com.bhsoft.ar3d.ui.base.activity.BaseMVVMActivity
import com.bhsoft.ar3d.ui.fragment.camera_fragment.Constants
import com.bhsoft.ar3d.ui.utils.OpenFragmentUtils

class MainActivity : BaseMVVMActivity<MainCallBack, MainViewModel>(), MainCallBack {

    override fun getLayoutMain() = R.layout.activity_main

    override fun setEvents() {
    }

    @SuppressLint("NewApi")
    override fun initComponents() {
        getBindingData().viewModel = mModel
        OpenFragmentUtils.openUserFragment(supportFragmentManager)

        CustomManagerAppAllFile()
        CustomReadStoragePermision()
        CustomCameraPermission()
        CustomWritePermission()
    }

    private fun CustomWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),Constants.REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }
    private fun CustomCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),Constants.REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun CustomManagerAppAllFile(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            }

        }
    }
    private fun CustomReadStoragePermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    override fun getBindingData() = mBinding as ActivityMainBinding

}