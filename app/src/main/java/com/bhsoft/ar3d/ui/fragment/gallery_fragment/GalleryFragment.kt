package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentGalleryBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment
import com.bhsoft.ar3d.ui.fragment.details_gallery_fragment.DetailsGalleryFragment
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.GalleryAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbBigAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbSmallAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.folder.FolderImageFragment
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop.GalleryImageCropFragment
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException


class GalleryFragment: BaseMvvmFragment<GalleryCallBack,GalleryViewModel>(),GalleryCallBack,
    GalleryAdapter.IImageGallery ,ThumbBigAdapter.IThumBig,ThumbSmallAdapter.IThumbSmall{
    private var dialog : Dialog?=null
    private var shareImage : Intent? = null
    override fun initComponents() {
        getBindingData().galleryViewModel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                GalleryViewModel.GET_DATA_IMAGE_SUCCESS -> getDataImageSuccess()
            }
        }
        initRecylerViewThumbBig()
        initRecyclerViewImage()
        initRecylerViewThumbSmall()
        mModel.getImages()
        setHasOptionsMenu(true)
        customToolbar()
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

    override fun getLayoutMain(): Int {
        return R.layout.fragment_gallery
    }

    override fun setEvents() {

    }

    override fun getBindingData() = mBinding as FragmentGalleryBinding

    override fun getViewModel(): Class<GalleryViewModel> {
        return GalleryViewModel::class.java
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
        val TAG = GalleryFragment::class.java.name
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
        val folderImageFragment = FolderImageFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content,folderImageFragment)
        fragmentTransaction.addToBackStack(FolderImageFragment.TAG)
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
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
           onClickShareNoImage(mModel.getFileImageList()[position].path)
        }
        dialog!!.show()
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onLongClickChangeThumbSmallImage(position: Int,imgThumbSmall : ImageView) {
        showDialog()
        val dialogRename = dialog!!.findViewById<LinearLayout>(R.id.layout_rename)
        val dialogDelete = dialog!!.findViewById<LinearLayout>(R.id.layout_delete)
        val dialogShare = dialog!!.findViewById<LinearLayout>(R.id.layout_share)
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
            onClickShareImage(mModel.getFileImageList()[position].path,imgThumbSmall)
        }
        dialog!!.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onLongClickChangeThumBigImage(position: Int,imgThumbBig : ImageView) {
        showDialog()
        val dialogRename = dialog!!.findViewById<LinearLayout>(R.id.layout_rename)
        val dialogDelete = dialog!!.findViewById<LinearLayout>(R.id.layout_delete)
        val dialogShare = dialog!!.findViewById<LinearLayout>(R.id.layout_share)
        dialogRename.setOnClickListener {
            mModel.onRenameFile(context!!,position)
            dialog!!.dismiss()
        }
        dialogDelete.setOnClickListener {
            mModel.getDeleteImage(context!!,mModel.getFileImageList()[position].path)
            dialog!!.dismiss()
        }
        dialogShare.setOnClickListener {
            dialog!!.dismiss()
            onClickShareImage(mModel.getFileImageList()[position].path,imgThumbBig)
        }
        dialog!!.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showDialog() {
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_change_image)
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
        mModel.getImages()
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
}