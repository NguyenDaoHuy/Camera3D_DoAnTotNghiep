package com.bhsoft.ar3d.ui.fragment.details_gallery_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Image
import com.bhsoft.ar3d.databinding.ItemImageThumbBigGalleryBinding

class ImageCropedAdapter(private val inters :IImageCroped)
    :RecyclerView.Adapter<ImageCropedAdapter.Companion.ThumbBigViewHolder>(){

    companion object{
        class ThumbBigViewHolder(val binding : ItemImageThumbBigGalleryBinding):RecyclerView.ViewHolder(binding.root)
    }
    interface IImageCroped{
        fun getCount():Int
        fun getImage(position:Int):Image
        fun onClickItem(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbBigViewHolder {
        val binding = ItemImageThumbBigGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ThumbBigViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbBigViewHolder, position: Int) {
        val image = inters.getImage(position)
        holder.binding.imgThumbBig.setImageBitmap(image.bitmap)
        holder.binding.txtNameFileThumbBig.text = image.name
        holder.itemView.setOnClickListener { inters.onClickItem(position) }
    }

    override fun getItemCount(): Int {
       return inters.getCount()
    }
}