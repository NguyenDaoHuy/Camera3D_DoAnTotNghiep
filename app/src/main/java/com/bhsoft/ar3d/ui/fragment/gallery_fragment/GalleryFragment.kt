package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.databinding.FragmentGalleryBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment

class GalleryFragment : BaseMvvmFragment<GalleryCallBack,GalleryViewModel>(),GalleryCallBack,GalleryAdapter.IImageGallery {

    override fun initComponents() {
        getBindingData().galleryViewModel = mModel
        mModel.getDataImageFromDevices("AR 3D",requireContext())
        initRecyclerViewImage()
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
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(context,3,RecyclerView.VERTICAL,false)
        getBindingData().rcvImage.layoutManager = layoutManager
        getBindingData().rcvImage.adapter = galleryAdapter
    }

    override fun count(): Int {
        if(mModel.getFilesImageList() == null){
            return 0
        }
        return mModel.getFilesImageList()!!.size
    }

    override fun getData(position: Int): Image {
        return mModel.getFilesImageList()!![position]
    }

    companion object{
        val TAG = GalleryFragment::class.java.name
    }

}