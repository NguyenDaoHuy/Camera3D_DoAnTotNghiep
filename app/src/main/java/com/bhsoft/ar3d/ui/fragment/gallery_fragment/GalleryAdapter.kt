package com.bhsoft.ar3d.ui.fragment.gallery_fragment

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.databinding.ItemImageFromGalleryBinding

class GalleryAdapter(private val inter: IImageGallery) :
    RecyclerView.Adapter<GalleryAdapter.Companion.ImageViewHolder>() {

    companion object {
        class ImageViewHolder(val binding: ItemImageFromGalleryBinding) : RecyclerView.ViewHolder(binding.root)
    }

    interface IImageGallery {
        fun count(): Int
        fun getData(position: Int): Image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageFromGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = inter.getData(position)
        holder.binding.image.setImageBitmap(BitmapFactory.decodeFile(image!!.path))
    }

    override fun getItemCount(): Int {
        return inter.count()
    }
}