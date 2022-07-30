package com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.ItemImageFromGalleryBinding

class GalleryAdapter(private val inter: IImageGallery) :
    RecyclerView.Adapter<GalleryAdapter.Companion.ImageViewHolder>() {

    companion object {
        class ImageViewHolder(val binding: ItemImageFromGalleryBinding) : RecyclerView.ViewHolder(binding.root)
    }

    interface IImageGallery {
        fun count(): Int
        fun getData(position: Int): Pictures
        fun onClickImageFileName(position: Int)
        fun onLongClickChangeFileNameImage(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageFromGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = inter.getData(position)
        holder.binding.txtNameFile.text = image.title
        holder.itemView.setOnClickListener {
            inter.onClickImageFileName(position)
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            inter.onLongClickChangeFileNameImage(position)
            true
        })
    }

    override fun getItemCount(): Int {
        return inter.count()
    }
}