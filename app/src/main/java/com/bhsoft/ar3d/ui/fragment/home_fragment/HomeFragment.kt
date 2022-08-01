package com.bhsoft.ar3d.ui.fragment.home_fragment

import android.Manifest
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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.FragmentHome2Binding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryFragment

class HomeFragment : BaseMvvmFragment<HomeCallBack,HomeViewModel>(),HomeCallBack {

    override fun initComponents() {
        getBindingData().homeViewMocel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                HomeViewModel.ON_CLICK_CAMERA -> goToCamera()
                HomeViewModel.ON_CLICK_GALLERY -> onCLickGallery()
            }
        }
    }
    private fun onCLickGallery(){
        goToGallery()
//        connectPermission()
    }

    private fun goToGallery() {
        val galleryFragment = GalleryFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content,galleryFragment)
        fragmentTransaction.addToBackStack(GalleryFragment.TAG)
        fragmentTransaction.commit()
    }

    private fun goToCamera() {
         val cameraFragment = CameraFragment()
         val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.content,cameraFragment)
         fragmentTransaction.addToBackStack(CameraFragment.TAG)
         fragmentTransaction.commit()
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_home2
    }

    override fun setEvents() {

    }

    override fun getBindingData() = mBinding as FragmentHome2Binding

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    private fun connectPermission() {
        if (checkPermission()) {

        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("App Permission")
                .setMessage("""
                     You must allow this aoo to access files on your device
    
                     Now follow the below steps
    
                     Open Settings from below button
                     Click on Permission
                     Allow access for storage
                     """.trimIndent()
                )
                .setPositiveButton("Open Settings") { dialog: DialogInterface?, which: Int -> requestPermission() }
                .create().show()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                Log.d(TAG, "requestPermission: catch")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION)
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result: ActivityResult? ->
        Log.d(TAG, "onActivityResult: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted")
                Toast.makeText(requireContext(), "Manage External Storage Permission is granted", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d(TAG, "onActivityResult: Manage External Storage Permission is denied")
            Toast.makeText(requireContext(), "Manage External Storage Permission is denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            )
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (write) {

                    Log.d(TAG, "onRequestPermissionResult: External Storage permission granted")
                } else {
                    Log.d(TAG, "onRequestPermissionResult: External Storage permission denied")
                    Toast.makeText(requireContext(), "External Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    companion object {
        private const val STORAGE_PERMISSION = 100
        private const val TAG = "PERMISSON_TAG"
    }
}