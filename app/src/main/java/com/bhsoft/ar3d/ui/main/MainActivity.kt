package com.bhsoft.ar3d.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhsoft.ar3d.BuildConfig
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityMainBinding
import com.bhsoft.ar3d.ui.base.activity.BaseMVVMActivity
import com.bhsoft.ar3d.ui.fragment.camera_fragment.Constants
import com.bhsoft.ar3d.ui.utils.OpenFragmentUtils

class MainActivity : BaseMVVMActivity<MainCallBack, MainViewModel>(), MainCallBack {
    private var dialog:AlertDialog?=null
    override fun getLayoutMain() = R.layout.activity_main

    override fun setEvents() {
    }

    @SuppressLint("NewApi")
    override fun initComponents() {
        getBindingData().viewModel = mModel
        OpenFragmentUtils.openUserFragment(supportFragmentManager)
//        showDialog()
        connectPermission()
        CustomCameraPermission()
//        CustomWritePermission()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()) {
//                dialog!!.dismiss()
//            }
//        }
    }

//    @SuppressLint("UseRequireInsteadOfGet")
//    private fun showDialog() {
//        dialog = Dialog(this)
//        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.setContentView(R.layout.dialog_confirm)
//        val txtTitle = dialog!!.findViewById<TextView>(R.id.tv_content)
//        txtTitle.text = resources.getString(R.string.You_need_open_setting_to_grant_permission)
//        val window = dialog!!.window ?: return
//        window.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val windowAtributes = window.attributes
//        windowAtributes.gravity = Gravity.CENTER
//        window.attributes = windowAtributes
//        if (Gravity.CENTER == Gravity.CENTER) {
//            dialog!!.setCancelable(true)
//        } else {
//            dialog!!.setCancelable(false)
//        }
//        val dialogOK = dialog!!.findViewById<Button>(R.id.btn_ok)
//        val dialogCancel = dialog!!.findViewById<Button>(R.id.btn_cancel)
//        dialogOK.setOnClickListener {
//            CustomManagerAppAllFile()
//            dialog!!.dismiss()
//        }
//        dialogCancel.setOnClickListener {
//            dialog!!.dismiss()
//        }
//        dialog!!.show()
//    }

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

//    private fun CustomManagerAppAllFile(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()){
//                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
//                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
//            }
//
//        }else{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&  checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.REQUEST_READ_EXTERNAL_STORAGE
//                )
//            }
//        }
//    }

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    override fun getBindingData() = mBinding as ActivityMainBinding

    private fun connectPermission() {
        if (checkPermission()) {

        } else {
           val builder = AlertDialog.Builder(this)
                    .setTitle("App Permission")
                     .setMessage("""
                     You must allow this aoo to access files on your device
    
                     Now follow the below steps
    
                     Open Settings from below button
                     Click on Permission
                     Allow access for storage
                     """.trimIndent()
                )
                .setNegativeButton("Cancel"){
                        dialog: DialogInterface?, which: Int ->
                    dialog!!.dismiss()
                }
                .setPositiveButton("Open Settings") { dialog: DialogInterface?, which: Int -> requestPermission() }
                dialog = builder.create()
                dialog?.show()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package",this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                Log.d(TAG, "requestPermission: catch")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION)
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result: ActivityResult? ->

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))

            }

        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&  checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.REQUEST_READ_EXTERNAL_STORAGE

                )
            }
        }
    }
    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (write) {

                } else {
                    Toast.makeText(this, "External Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    companion object {
        private const val STORAGE_PERMISSION = 100
        private const val TAG = "PERMISSON_TAG"
    }

}