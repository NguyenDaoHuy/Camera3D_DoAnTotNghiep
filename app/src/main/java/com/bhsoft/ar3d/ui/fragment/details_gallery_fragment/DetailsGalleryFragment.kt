package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.StrictMode
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentDetailsGalleryBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class DetailsGalleryFragment:BaseMvvmFragment<DetailsGalleryCallBack,DetailsGalleryViewModel>(),DetailsGalleryCallBack{
   private var pictures : Pictures ?=null
    private var dialog : Dialog?=null
    private var progressDialog : ProgressDialog?=null
    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }

    override fun getLayoutMain(): Int {
        return R.layout.fragment_details_gallery
    }

    override fun setEvents() {

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initComponents() {
        getBindingData().detailsViewModel = mModel
        progressDialog = ProgressDialog(context)
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                DetailsGalleryViewModel.ON_CLICK_DELETE -> onClickDeleteImage()
                DetailsGalleryViewModel.ON_CLICK_DETECT -> onClickDetectImage()
                DetailsGalleryViewModel.ON_DELETE_SUCCESS -> backStack()
                DetailsGalleryViewModel.ON_CLICK_SHARE -> onClickShareImage()
                DetailsGalleryViewModel.PROGRESS_DIALOG -> showProgressDialog()
                DetailsGalleryViewModel.PROGRESS_DIALOG_DISSMISS -> onDismissDialog()
                DetailsGalleryViewModel.ON_VISIBLE_BUTTON -> onVisibleButton()
                DetailsGalleryViewModel.ON_TOAST_BOXES_NULL -> onToastBoxesNull()
                DetailsGalleryViewModel.ON_CLICK_CROP_IMAGE -> onClickCropImage()
            }
        }

        pictures = arguments!!.getSerializable("details") as Pictures?
        Glide.with(context!!).load(pictures!!.path).into(getBindingData().imgDetails)
        onClickToBack()
    }

    private fun onDismissDialog() {
        progressDialog!!.dismiss()
    }

    private fun showProgressDialog() {
        progressDialog!!.setMessage("Please wait.......")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    private fun onClickDetectImage() {
        getBindingData().txtOutput.visibility = View.VISIBLE
        mModel.imgInput = getBindingData().imgDetails
        mModel.txtOutput = getBindingData().txtOutput
        val drawable = getBindingData().imgDetails.drawable as BitmapDrawable
        val bitMap = drawable.bitmap
        mModel.runClassfication(bitMap)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun onClickDeleteImage() {
       showDialog()
       val dialogOK = dialog!!.findViewById<Button>(R.id.btn_ok)
        val dialogCancel = dialog!!.findViewById<Button>(R.id.btn_cancel)
        dialogOK.setOnClickListener {
            mModel.clickDeleteImage(pictures!!.path)
            dialog!!.dismiss()
        }
        dialogCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showDialog() {
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_confirm)
        val txtTitle = dialog!!.findViewById<TextView>(R.id.tv_content)
        txtTitle.text = resources.getString(R.string.Title_confirm_dialog_delete_image)
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

    @SuppressLint("UseRequireInsteadOfGet")
    private fun onClickToBack() {
        getBindingData().btnBack.setOnClickListener {
            backStack()
        }
    }
    override fun getBindingData() = mBinding as FragmentDetailsGalleryBinding

    override fun getViewModel(): Class<DetailsGalleryViewModel> {
        return DetailsGalleryViewModel::class.java
    }
    companion object{
        const val TAG = "Details"
    }
    fun backStack(){
        requireActivity().supportFragmentManager!!.popBackStack()
    }
    fun onClickShareImage(){
        val builder : StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = File(pictures!!.path)
        val shareImage : Intent
        try{
            val fileOutputStream = FileOutputStream(file)
            val uri : Uri = Uri.fromFile(file)
            val drawable = getBindingData().imgDetails.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
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
    private fun onClickCropImage(){
        val drawable = getBindingData().imgDetails.drawable as BitmapDrawable
        val bitMap = drawable.bitmap
        mModel.cropImage(bitMap)
        showDialogImageCroped()
    }
    fun onVisibleButton(){
        getBindingData().btnCrop.visibility = View.VISIBLE
    }
    fun onToastBoxesNull(){
        Toast.makeText(requireContext(),"No objects found",Toast.LENGTH_SHORT).show()
    }
    fun showDialogImageCroped(){
        val view = View.inflate(context, R.layout.dialog_list_image_croped, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(false)
        val btnCancel  = view.findViewById<Button>(R.id.btnCancel)
        val btnAddGallery  = view.findViewById<Button>(R.id.btnAddGallery)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnAddGallery.setOnClickListener {

        }
    }
}