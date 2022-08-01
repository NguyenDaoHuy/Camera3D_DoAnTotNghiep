package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentDetailsGalleryBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bumptech.glide.Glide

class DetailsGalleryFragment:BaseMvvmFragment<DetailsGalleryCallBack,DetailsGalleryViewModel>(),DetailsGalleryCallBack{
   private var pictures : Pictures ?=null
    private var dialog : Dialog?=null
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
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
                DetailsGalleryViewModel.ON_CLICK_DELETE -> onClickDeleteImage()
                DetailsGalleryViewModel.ON_CLICK_DETECT -> onClickDetectImage()
            }
        }
        pictures = arguments!!.getSerializable("details") as Pictures?
        Glide.with(context!!).load(pictures!!.path).into(getBindingData().imgDetails)
        onClickToBack()
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
            mModel.clickDeleteImage(context!!,pictures!!.path)
            dialog!!.dismiss()
            finishActivity()
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
            fragmentManager!!.popBackStack()
        }
    }
    override fun getBindingData() = mBinding as FragmentDetailsGalleryBinding

    override fun getViewModel(): Class<DetailsGalleryViewModel> {
        return DetailsGalleryViewModel::class.java
    }
    companion object{
        const val TAG = "Details"
    }
}