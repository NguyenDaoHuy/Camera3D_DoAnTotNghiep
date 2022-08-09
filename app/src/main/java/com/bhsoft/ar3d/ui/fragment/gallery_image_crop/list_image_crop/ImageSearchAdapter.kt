package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.ItemImageThumbBigGalleryBinding
import com.bhsoft.ar3d.databinding.ItemImageThumbSmallGalleryBinding
import com.bumptech.glide.Glide

class ImageSearchAdapter(private val inters : IImageSearch)
    :RecyclerView.Adapter<ImageSearchAdapter.Companion.ThumbBigViewHolder>(){

    companion object{
        class ThumbBigViewHolder(val binding : ItemImageThumbSmallGalleryBinding):RecyclerView.ViewHolder(binding.root)
    }
    interface IImageSearch{
        fun getCount():Int
        fun getImage(position:Int):Pictures
        fun getContextImageSearch():Context
        fun onClickImageSearch(position: Int)
        fun onLongClickImageSearch(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbBigViewHolder {
        val binding = ItemImageThumbSmallGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ThumbBigViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbBigViewHolder, position: Int) {
        val img = inters.getImage(position)
        holder.binding.txtNameFileThumbSmall.text = img.title
        Glide.with(inters.getContextImageSearch()).load(img.path).into(holder.binding.imgThumbSmall)
        holder.binding.txtLength.text = Formatter.formatFileSize(holder.binding.txtLength.context,img.sizes)
        holder.itemView.setOnClickListener {
            inters.onClickImageSearch(position)
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            inters.onLongClickImageSearch(position)
            true
        })
    }

    override fun getItemCount(): Int {
       return inters.getCount()
    }
}