package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.FragmentGalleryBinding
import com.bhsoft.ar3d.ui.base.fragment.BaseMvvmFragment
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.GalleryAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbBigAdapter
import com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter.ThumbSmallAdapter


class GalleryFragment : BaseMvvmFragment<GalleryCallBack,GalleryViewModel>(),GalleryCallBack,
    GalleryAdapter.IImageGallery ,ThumbBigAdapter.IThumBig,ThumbSmallAdapter.IThumbSmall{

    override fun initComponents() {
        getBindingData().galleryViewModel = mModel
        mModel.getImages()
        initRecyclerViewImage()
        setHasOptionsMenu(true)
        customToolbar()
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
        }
        return true
    }

    private fun showListViewThumbnailBig() {
        initRecylerViewThumbBig()
        getBindingData().recylerThumbnailBig.visibility = View.VISIBLE
        getBindingData().recylerFileName.visibility = View.GONE
        getBindingData().recylerThumbnailSmall.visibility = View.GONE
    }

    private fun showListViewThumbnailSmall() {
        initRecylerViewThumbSmall()
        getBindingData().recylerThumbnailSmall.visibility = View.VISIBLE
        getBindingData().recylerFileName.visibility = View.GONE
        getBindingData().recylerThumbnailBig.visibility = View.GONE
    }

    private fun showListViewNameFile() {
        initRecyclerViewImage()
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
}