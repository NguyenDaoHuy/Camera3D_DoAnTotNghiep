package com.bhsoft.ar3d.ui.activity.gallery_edit;

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityGallatyBinding
import com.bhsoft.ar3d.ui.activity.EditingActivity
import com.bhsoft.ar3d.ui.utils.Utility
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class GallaryActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 24
    lateinit var binding: ActivityGallatyBinding
    private var mapItem: HashMap<String, ArrayList<String>>? = null
    private lateinit var listFolder: Array<String>
    private var folderAdapter: ArrayAdapter<String>? = null
    private var imageAdapter: ImagePickerAdapter? = null
    private val layoutManager = GridLayoutManager(this, 3)
    var mPath: String? = null
    var mCurrentPhotoPath: String? = null
    private var imageFromCameraUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGallatyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rcvImage.layoutManager = layoutManager
        mapItem = ImageManager.getAllImageFromDevices(this)
        val keySet: Set<String> = mapItem!!.keys
        listFolder = keySet.toTypedArray()
        setFolderAdapter()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setFolderAdapter() {
        folderAdapter = ArrayAdapter(this, com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, listFolder)
        folderAdapter!!.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item)
        binding.spFolder.adapter = folderAdapter
        binding.spFolder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (listFolder[position] != "All") {
                        setAdapter(listFolder[position])
                    } else {
                        imageAdapter = ImagePickerAdapter(this@GallaryActivity,object :ImagePickerAdapter.OnClickListener{
                            override fun onClick(path: String?,camera : Boolean, position: Int) {
                                if (camera){
                                 //   startCaptureImage(REQUEST_IMAGE_CAPTURE)
                                    openCamera()
                                    Toast.makeText(this@GallaryActivity,"Update",Toast.LENGTH_SHORT).show()
                                }else{
                                    mPath = path
                                    imageAdapter?.onClickItem(position)
                                    val uri = Uri.fromFile(File(path))
                                    startCropActivity(uri)
                                }
                            }
                        })
                        imageAdapter!!.setData(ImageManager.getAllImageFromDevicesAll(this@GallaryActivity))
                        binding.rcvImage.adapter = imageAdapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setAdapter(folder: String) {
        imageAdapter = ImagePickerAdapter(this,object :ImagePickerAdapter.OnClickListener{
            override fun onClick(path: String?,camera : Boolean, position: Int) {
                if (camera){
                    startCaptureImage(REQUEST_IMAGE_CAPTURE)
                }else{
                    mPath = path
                    imageAdapter?.onClickItem(position)
                    val uri = Uri.fromFile(File(path))
                    startCropActivity(uri)
                }
            }
        })
        imageAdapter!!.setData(mapItem!![folder]!!)
        binding.rcvImage.adapter = imageAdapter
    }

    private fun startCaptureImage(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.e("MainActivity.TAG", "startCaptureImage: " + ex.message)
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UCrop.REQUEST_CROP){
            data?.let { handleCropResult(it) }
        }else{
            if (data != null) {
                handleCropError(data)
            }
        }

        if (requestCode === REQUEST_IMAGE_CAPTURE && resultCode === RESULT_OK) {
            imageFromCameraUri?.let { startCropActivity(it) }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun galleryAddPic(path: String?) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(path)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    companion object {
        private const val RC_CROP_IMAGE = 102
    }

    private fun startCropActivity(data: Uri) {
        var destination_file_name = "crop"
        destination_file_name += ".jpg"
        val uCrop = UCrop.of(data, Uri.fromFile(File(cacheDir, destination_file_name)))
        val options = UCrop.Options()
        options.setToolbarColor(resources.getColor(R.color.black))
        options.setToolbarWidgetColor(resources.getColor(R.color.white))
        options.setStatusBarColor(resources.getColor(R.color.white))
        options.setActiveWidgetColor(resources.getColor(R.color.blue_crop))
        options.setLogoColor(resources.getColor(R.color.white))
        options.setToolbarTitle("Crop")
        uCrop.withOptions(options)
        uCrop.start(this)
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            try {
                Utility.gallery_bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                Utility.isNetworkAvailable(applicationContext)
                Utility.easer_bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                val intent2 = Intent(this, EditingActivity::class.java)
                startActivity(intent2)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Failed To Crop", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Toast.makeText(this, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "unexpected_error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir)
            imageFromCameraUri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFromCameraUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }
}