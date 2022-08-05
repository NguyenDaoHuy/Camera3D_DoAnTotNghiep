package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.folder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentFolderImageBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import com.bhsoft.ar3d.ui.fragment.camera_fragment.CameraFragment
import com.bhsoft.ar3d.ui.fragment.details_gallery_fragment.DetailsGalleryFragment
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.GalleryFragment
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop.GalleryImageCropFragment

class FolderImageFragment : BaseMvvmFragment<FolderImageCallBack,FolderImageViewModel>(),FolderImageCallBack,
    FolderAdapter.FolderInterface {

    override fun initComponents() {
        getBindingData().folderImageViewModel = mModel
        mModel.uiEventLiveData.observe(this){
            when(it){
                BaseViewModel.FINISH_ACTIVITY -> finishActivity()
            }
        }
        onSearch()
        setHasOptionsMenu(true)
        customToolbar()
        mModel.getFolder("")
        initRecycleview()
    }

    private fun onSearch(){
        getBindingData().searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {
                mModel.getFolder(newText)
                getBindingData().rcvFolderImageCrop.adapter!!.notifyDataSetChanged()
                return false
            }
        })
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun customToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(getBindingData()!!.toolBarGallery)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getBindingData()!!.toolBarGallery.setNavigationOnClickListener { v ->
            fragmentManager!!.popBackStack()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallery_folder_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    fun initRecycleview() {
        val adapter = FolderAdapter(this)
        getBindingData().rcvFolderImageCrop.adapter = adapter
        getBindingData().rcvFolderImageCrop.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter.notifyDataSetChanged()
    }
    override fun getLayoutMain(): Int {
        return R.layout.fragment_folder_image
    }

    override fun setEvents() {

    }

    override fun getBindingData() = mBinding as FragmentFolderImageBinding

    override fun getViewModel(): Class<FolderImageViewModel> {
        return FolderImageViewModel::class.java
    }
    override fun error(id: String, error: Throwable) {
        showMessage(error.message!!)
    }
    companion object{
        val TAG = FolderImageFragment::class.java.name
    }

    override fun getCount(): Int? {
        return mModel.getFileImageList().size
    }

    override fun file(position: Int): Pictures? {
        return mModel.getFileImageList().get(position)
    }

    override fun onClickItem(position: Int) {
        val pictures = mModel.getFileImageList().get(position)
        val galleryImageCropFragment = GalleryImageCropFragment()
        val bundle = Bundle()
        bundle.putSerializable("folder",pictures)
        galleryImageCropFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.replace(R.id.content, galleryImageCropFragment!!)
        transaction!!.addToBackStack(GalleryImageCropFragment.TAG)
        transaction!!.commit()
    }
}