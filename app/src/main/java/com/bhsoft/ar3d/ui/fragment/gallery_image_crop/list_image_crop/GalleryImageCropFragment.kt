package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.BoxLable
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentGalleryImageCropBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment
import com.bhsoft.ar3d.ui.fragment.details_gallery_fragment.DetailsGalleryFragment
import com.bhsoft.ar3d.ui.fragment.details_gallery_fragment.DetailsGalleryViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryFragment
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryViewModel
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.GalleryAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbBigAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbSmallAdapter
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException
import java.util.ArrayList
import java.util.Comparator

class GalleryImageCropFragment: BaseMvvmFragment<GalleryImageCropCallBack, GalleryImageCropViewModel>(),
    GalleryImageCropCallBack,
    GalleryAdapter.IImageGallery ,ThumbBigAdapter.IThumBig,ThumbSmallAdapter.IThumbSmall,
    ImageSearchAdapter.IImageSearch {
    private var dialog : Dialog?=null
    private var folder : Pictures ?=null
    private var compareImageList : ArrayList<Pictures>? = null
    private var labeler  : ImageLabeler?=null
 //   private var inputImage : InputImage?=null
    private var dialogImageCroper : AlertDialog?=null
    private var progressDialog : ProgressDialog?=null
    private var objectDetector : ObjectDetector?=null
    private var strImageSearch : String = ""

    override fun initComponents() {
        getBindingData().galleryImageCropViewModel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                GalleryViewModel.GET_DATA_IMAGE_SUCCESS -> getDataImageSuccess()
            }
        }
        progressDialog = ProgressDialog(context)
        folder = requireArguments().getSerializable("folder") as Pictures?
        initRecylerViewThumbBig()
        initRecyclerViewImage()
        initRecylerViewThumbSmall()
        mModel.getImages("",folder!!.title)
        setHasOptionsMenu(true)
        customToolbar()

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        objectDetector = ObjectDetection.getClient(options)

        val option = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
        labeler = ImageLabeling.getClient(option)
    }

    private fun getDataImageSuccess() {
        getBindingData().recylerFileName.adapter!!.notifyDataSetChanged()
        getBindingData().recylerThumbnailBig.adapter!!.notifyDataSetChanged()
        getBindingData().recylerThumbnailSmall.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun customToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(getBindingData()!!.toolBarGallery)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getBindingData()!!.toolBarGallery.setNavigationOnClickListener { v ->
            fragmentManager!!.popBackStack()
        }
    }

    private fun showProgressDialog() {
        progressDialog!!.setMessage("Searching .......")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_gallery_image_crop
    }

    override fun setEvents() {

    }

    override fun getBindingData() = mBinding as FragmentGalleryImageCropBinding

    override fun getViewModel(): Class<GalleryImageCropViewModel> {
        return GalleryImageCropViewModel::class.java
    }

    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    private fun initRecyclerViewImage(){
        val galleryAdapter = GalleryAdapter(this)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        getBindingData().recylerFileName.layoutManager = layoutManager
        getBindingData().recylerFileName.adapter = galleryAdapter
    }
    private fun initRecylerViewThumbSmall(){
        val thumbSmallAdapter = ThumbSmallAdapter(this)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        getBindingData().recylerThumbnailSmall.layoutManager = layoutManager
        getBindingData().recylerThumbnailSmall.adapter = thumbSmallAdapter
    }
    private fun initRecylerViewThumbBig(){
        val thumbBigAdapter = ThumbBigAdapter(this)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(context,3,RecyclerView.VERTICAL,false)
        getBindingData().recylerThumbnailBig.layoutManager = layoutManager
        getBindingData().recylerThumbnailBig.adapter = thumbBigAdapter
    }
    companion object{
        val TAG = GalleryImageCropFragment::class.java.name
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallery_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.listViewNameFile->
                showListViewNameFile()
            R.id.listViewThumbnailSmall ->
                showListViewThumbnailSmall()
            R.id.listViewThumbnailBig ->
                showListViewThumbnailBig()
            R.id.changeGallery ->
                changeToGallery()
            R.id.goToCamera ->
                goCamera()
        }
        return true
    }
    private fun changeToGallery(){
        val galleryFragment = GalleryFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content,galleryFragment)
        fragmentTransaction.addToBackStack(GalleryFragment.TAG)
        fragmentTransaction.commit()
    }
    private fun goCamera(){
        val cameraFragment = CameraFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content,cameraFragment)
        fragmentTransaction.addToBackStack(CameraFragment.TAG)
        fragmentTransaction.commit()
    }

    private fun showListViewThumbnailBig() {
        getBindingData().recylerThumbnailBig.visibility = View.VISIBLE
        getBindingData().recylerFileName.visibility = View.GONE
        getBindingData().recylerThumbnailSmall.visibility = View.GONE
    }

    private fun showListViewThumbnailSmall() {
        getBindingData().recylerThumbnailSmall.visibility = View.VISIBLE
        getBindingData().recylerFileName.visibility = View.GONE
        getBindingData().recylerThumbnailBig.visibility = View.GONE
    }

    private fun showListViewNameFile() {
        getBindingData().recylerFileName.visibility = View.VISIBLE
        getBindingData().recylerThumbnailBig.visibility = View.GONE
        getBindingData().recylerThumbnailSmall.visibility = View.GONE

    }

    override fun count(): Int {
        return mModel.getFileImageList().size
    }

    override fun getData(position: Int): Pictures {
        return mModel.getFileImageList()[position]
    }


    override fun getCountBig(): Int {
        return mModel.getFileImageList().size
    }

    override fun getDataBig(position: Int): Pictures {
        return mModel.getFileImageList()[position]
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getContextBig(): Context {
        return context!!
    }
    override fun getCountSmall(): Int {
        return mModel.getFileImageList().size
    }

    override fun getDataSmall(position: Int): Pictures {
        return mModel.getFileImageList()[position]
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getContextSmall(): Context {
        return context!!
    }

    override fun onClickItem(position: Int) {
        //Click image to details
        val detailsFragment = DetailsGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable("details", mModel.getFileImageList()[position])
        detailsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.content, detailsFragment!!)
        transaction!!.addToBackStack(DetailsGalleryFragment.TAG)
        transaction!!.commit()
    }



    override fun onClickImageFileName(position: Int) {
        val detailsFragment = DetailsGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable("details", mModel.getFileImageList()[position])
        detailsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.content, detailsFragment!!)
        transaction!!.addToBackStack(DetailsGalleryFragment.TAG)
        transaction!!.commit()
    }

    override fun onClickItemThumBig(position: Int) {
        val detailsFragment = DetailsGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable("details", mModel.getFileImageList()[position])
        detailsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.content, detailsFragment!!)
        transaction!!.addToBackStack(DetailsGalleryFragment.TAG)
        transaction!!.commit()
    }



    @SuppressLint("UseRequireInsteadOfGet")
    override fun onLongClickChangeFileNameImage(position: Int) {
        showDialog()
        val dialogRename = dialog!!.findViewById<LinearLayout>(R.id.layout_rename)
        val dialogDelete = dialog!!.findViewById<LinearLayout>(R.id.layout_delete)
        val dialogShare = dialog!!.findViewById<LinearLayout>(R.id.layout_share)
        val dialogSearch = dialog!!.findViewById<LinearLayout>(R.id.layout_search)
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position,folder!!.title)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path,folder!!.title)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
            onClickShareNoImage(mModel.getFileImageList()[position].path)
        }
        dialogSearch.setOnClickListener {
            dialog!!.dismiss()
            searchImageCroped(mModel.getFileImageList()[position])
        }
        dialog!!.show()
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onLongClickChangeThumbSmallImage(position: Int,imgThumbSmall : ImageView) {
        showDialog()
        val dialogRename = dialog!!.findViewById<LinearLayout>(R.id.layout_rename)
        val dialogDelete = dialog!!.findViewById<LinearLayout>(R.id.layout_delete)
        val dialogShare = dialog!!.findViewById<LinearLayout>(R.id.layout_share)
        val dialogSearch = dialog!!.findViewById<LinearLayout>(R.id.layout_search)
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position,folder!!.title)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path,folder!!.title)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
            onClickShareImage(mModel.getFileImageList()[position].path,imgThumbSmall)
        }
        dialogSearch.setOnClickListener {
            dialog!!.dismiss()
            searchImageCroped(mModel.getFileImageList()[position])
        }
        dialog!!.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onLongClickChangeThumBigImage(position: Int,imgThumbBig : ImageView) {
        showDialog()
        val dialogRename = dialog!!.findViewById<LinearLayout>(R.id.layout_rename)
        val dialogDelete = dialog!!.findViewById<LinearLayout>(R.id.layout_delete)
        val dialogShare = dialog!!.findViewById<LinearLayout>(R.id.layout_share)
        val dialogSearch = dialog!!.findViewById<LinearLayout>(R.id.layout_search)
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position,folder!!.title)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path,folder!!.title)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
            onClickShareImage(mModel.getFileImageList()[position].path,imgThumbBig)
        }
        dialogSearch.setOnClickListener {
            dialog!!.dismiss()
            searchImageCroped(mModel.getFileImageList()[position])
        }
        dialog!!.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showDialog() {
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_change_image_2)
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
    }

    override fun onResumeControl() {
        super.onResumeControl()
        mModel.getImages("",folder!!.title)
    }

    fun onClickShareImage(path : String,imgThumbSmall : ImageView){
        val builder : StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = File(path)
        val shareImage : Intent
        try{
            val fileOutputStream = FileOutputStream(file)
            val uri : Uri = Uri.fromFile(file)
            val drawable = imgThumbSmall.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            shareImage = Intent(Intent.ACTION_SEND)
            shareImage.type = "image/*"
            shareImage.putExtra(Intent.EXTRA_STREAM,uri)
            shareImage.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }catch (e : Exception){
            throw RuntimeException(e)
        }
        startActivity(Intent.createChooser(shareImage,"Share Image"))
    }
    fun onClickShareNoImage(path : String){
        val builder : StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = File(path)
        val shareImage : Intent
        try{
            val fileOutputStream = FileOutputStream(file)
            val uri : Uri = Uri.fromFile(file)
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            shareImage = Intent(Intent.ACTION_SEND)
            shareImage.setType("image/*")
            shareImage.putExtra(Intent.EXTRA_STREAM,uri)
            shareImage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }catch (e : Exception){
            throw RuntimeException(e)
        }
        startActivity(Intent.createChooser(shareImage,"Share Image"))
    }

    fun searchImageCroped(pictures: Pictures){
        showProgressDialog()
        compareImageList = ArrayList()
        val index = pictures.title.indexOf("_")
        val subString = pictures.title.substring(0, index)
        getCompareImages3(subString,0)
        Toast.makeText(context,subString,Toast.LENGTH_SHORT).show()
    }

    fun getCompareImages(imageName: String) {
        compareImageList = ArrayList()
        val filePath = "/storage/emulated/0/DCIM/ar3d"
        val file = File(filePath)
        val files = file.listFiles()
        if (files != null) {
            for (file1: File in files) {
                println(files.size)
                if (file1.path.endsWith(".png") || file1.path.endsWith(".jpg")) {
                    // get Bitmap of Image
                    val file = File(file1.path)
                    val uri: Uri = Uri.fromFile(file)
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
                    val inputImage = InputImage.fromBitmap(bitmap, 0)

                    objectDetector!!.process(inputImage).addOnSuccessListener { detectedObjects ->
                        if (detectedObjects.isNotEmpty()){
                            var i = 0
                            for (`object` in detectedObjects) {
                                val bounds = `object`.boundingBox
                                val croppedBitmap = Bitmap.createBitmap(bitmap!!,bounds.left,bounds.top,bounds.width(),bounds.height())
                                var inputImage2 = InputImage.fromBitmap(croppedBitmap,0)
                                labeler!!.process(inputImage2).addOnSuccessListener { imageLabels ->
                                    i++
                                    println("=======================================>" + imageLabels.count())
                                    if (imageLabels.count()>0){
                                        for (labels in imageLabels){
                                            println("=============================================== 1 --->" + labels.text)
                                            println("=============================================== 2 --->" + imageName)
                                            if (labels.text == imageName){
//                                                compareImageList!!.add(Pictures(file1.path, file1.name, file1.length()))
//                                                println("=============================================== 3 --->" + compareImageList!!.size)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun getCompareImages2(imageName: String,possition : Int){
        val filePath = "/storage/emulated/0/DCIM/ar3d"
        val file = File(filePath)
        val files = file.listFiles()
        if (files != null) {
          val file1: File = files.get(possition)
            if (file1.path.endsWith(".png") || file1.path.endsWith(".jpg")) {
                val file = File(file1.path)
                val uri: Uri = Uri.fromFile(file)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
                val inputImage = InputImage.fromBitmap(bitmap, 0)
                var possition1 = possition + 1
                println("=============================================== Ảnh thứ " + possition1)
                objectDetector!!.process(inputImage).addOnSuccessListener { detectedObjects ->
                    if (detectedObjects.isNotEmpty()){
                        for (`object` in detectedObjects) {
                            val bounds = `object`.boundingBox
                            val croppedBitmap = Bitmap.createBitmap(bitmap!!,bounds.left,bounds.top,bounds.width(),bounds.height())
                            var inputImage2 = InputImage.fromBitmap(croppedBitmap,0)
                              labeler!!.process(inputImage2).addOnSuccessListener { imageLabels ->
                                  println("=======================================>" + imageLabels.count())
                                  for (labels in imageLabels){
                                      println("=============================================== 1 --->" + labels.text)
                                      println("=============================================== 2 --->" + imageName)
                                        if (labels.text == imageName){
                                            compareImageList!!.add(Pictures(file1.path, file1.name, file1.length()))
                                            println("=============================================== Searchs Size  --->" + compareImageList!!.size)
                                        }
                                  }
                             }
                        }
                    }
                    if(possition1 == files.size){
                        showDialogImageSearch()
                        progressDialog!!.dismiss()
                    }else{
                        getCompareImages2(imageName, possition1)
                    }
                }
            }
        }
    }
    fun getCompareImages3(imageName: String, possition: Int) {
        val filePath = "/storage/emulated/0/DCIM/ar3d"
        val file = File(filePath)
        val files = file.listFiles()
        if (files != null) {
            val file1: File = files.get(possition)
            if (file1.path.endsWith(".png") || file1.path.endsWith(".jpg")) {
                val file = File(file1.path)
                val uri: Uri = Uri.fromFile(file)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
                val inputImage = InputImage.fromBitmap(bitmap, 0)
                var possition1 = possition + 1
                labeler!!.process(inputImage).addOnSuccessListener { imageLabels ->
                    for (labels in imageLabels) {
                        if (labels.text == imageName) {
                            compareImageList!!.add(Pictures(file1.path, file1.name, file1.length()))
                            break
                        }
                    }
                    if (possition1 == files.size) {
                        showDialogImageSearch()
                        progressDialog!!.dismiss()
                    } else {
                        getCompareImages3(imageName, possition1)
                    }
                }
            }
        }
    }

    fun showDialogImageSearch(){
        val view = View.inflate(context, R.layout.dialog_list_image_search, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialogImageCroper = builder.create()
        dialogImageCroper!!.show()
        dialogImageCroper!!.window?.setBackgroundDrawableResource(R.color.transparent)
        dialogImageCroper!!.setCancelable(false)
        if (Gravity.CENTER == Gravity.CENTER) {
            dialogImageCroper!!.setCancelable(true)
        } else {
            dialogImageCroper!!.setCancelable(false)
        }
        val btnCancel  = view.findViewById<Button>(R.id.btnCancel)
        val rcvImageSearch  = view.findViewById<RecyclerView>(R.id.rcvImageCroped)
        val txtImageListSize = view.findViewById<TextView>(R.id.txtImageListSize)
        initRecyclerViewDialog(rcvImageSearch)
        if (compareImageList!!.isEmpty()) {
            strImageSearch = "Not found ..."
        } else {
            strImageSearch = "There were " + compareImageList!!.size + " results found"
        }
        txtImageListSize.setText(strImageSearch)
        btnCancel.setOnClickListener {
            dialogImageCroper!!.dismiss()
        }
    }

    override fun getCount(): Int {
        return compareImageList!!.size
    }

    override fun getImage(position: Int): Pictures {
        return compareImageList!!.get(position)
    }

    override fun getContextImageSearch(): Context {
        return requireContext()
    }

    override fun onClickImageSearch(position: Int) {
        val image = compareImageList!!.get(position)
        val view = View.inflate(context, R.layout.dialog_image_crop_details, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(false)
        if (Gravity.CENTER == Gravity.CENTER) {
            dialog!!.setCancelable(true)
        } else {
            dialog!!.setCancelable(false)
        }
        val imgImgaeCroped  = view.findViewById<ImageView>(R.id.imgImgaeCroped)
        val btnCanCelDialogImageCrop  = view.findViewById<ImageView>(R.id.btnCanCelDialogImageCrop)
        Glide.with(requireContext()).load(image.path).into(imgImgaeCroped)
        btnCanCelDialogImageCrop.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onLongClickImageSearch(position: Int) {
        val detailsFragment = DetailsGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable("details", compareImageList!!.get(position))
        detailsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.content, detailsFragment!!)
        transaction!!.addToBackStack(DetailsGalleryFragment.TAG)
        transaction!!.commit()
        dialogImageCroper!!.dismiss()
    }

    fun initRecyclerViewDialog(recyclerView: RecyclerView){
        val imageSearchAdapter= ImageSearchAdapter(this)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(context,1, RecyclerView.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = imageSearchAdapter
    }

}