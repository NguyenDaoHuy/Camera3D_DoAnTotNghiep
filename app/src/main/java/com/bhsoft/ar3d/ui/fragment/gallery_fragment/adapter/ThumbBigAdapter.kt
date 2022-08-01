package com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.ItemImageThumbBigGalleryBinding
import com.bumptech.glide.Glide

class ThumbBigAdapter(private val inters :IThumBig)
    :RecyclerView.Adapter<ThumbBigAdapter.Companion.ThumbBigViewHolder>(){

    companion object{
        class ThumbBigViewHolder(val binding : ItemImageThumbBigGalleryBinding):RecyclerView.ViewHolder(binding.root)
    }
    interface IThumBig{
        fun getCountBig():Int
        fun getDataBig(position:Int):Pictures
        fun getContextBig():Context
        fun onClickItemThumBig(position: Int)
        fun onLongClickChangeThumBigImage(position: Int,imgThumbBig : ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbBigViewHolder {
        val binding = ItemImageThumbBigGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ThumbBigViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbBigViewHolder, position: Int) {
        val img = inters.getDataBig(position)
        holder.binding.txtNameFileThumbBig.text = img.title
        Glide.with(inters.getContextBig()).load(img.path).into(holder.binding.imgThumbBig)
        holder.itemView.setOnClickListener {
            inters.onClickItemThumBig(position)
        }
        holder.itemView.setOnLongClickListener {
            inters.onLongClickChangeThumBigImage(position,holder.binding.imgThumbBig)
            true
        }
    }

    override fun getItemCount(): Int {
       return inters.getCountBig()
    }
}