package com.bhsoft.ar3d.ui.fragment.gallery_fragment.adapter

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.ItemImageThumbSmallGalleryBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.Format
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class ThumbSmallAdapter(private val inters : IThumbSmall)
    :RecyclerView.Adapter<ThumbSmallAdapter.Companion.ThumbSmallViewHolder>(){

    companion object{
        class ThumbSmallViewHolder(val binding : ItemImageThumbSmallGalleryBinding) : RecyclerView.ViewHolder(binding.root)
    }
    interface IThumbSmall{
        fun getCountSmall() : Int
        fun getDataSmall(position:Int):Pictures
        fun getContextSmall():Context
        fun onClickItem(position: Int)
        fun onLongClickChangeThumbSmallImage(position: Int,imgThumbSmall : ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbSmallViewHolder {
        val binding = ItemImageThumbSmallGalleryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ThumbSmallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbSmallViewHolder, position: Int) {
      val pics = inters.getDataSmall(position)
      holder.binding.txtNameFileThumbSmall.text = pics.title
//      holder.binding.txtLength.text = getSize(pics.sizes.toInt())
      holder.binding.txtLength.text = Formatter.formatFileSize(holder.binding.txtLength.context,pics.sizes)
      Glide.with(inters.getContextSmall()).load(pics.path).into(holder.binding.imgThumbSmall)
        holder.itemView.setOnClickListener {
            inters.onClickItem(position)
        }
        holder.itemView.setOnLongClickListener {
            inters.onLongClickChangeThumbSmallImage(position,holder.binding.imgThumbSmall)
            true
        }
    }

    override fun getItemCount(): Int {
       return inters.getCountSmall()
    }
    fun getSize(size : Int):String{
        if (size<=0){
            return "0"
        }
        val d = size
        val long10 = (log10(d.toDouble()) / log10(1024.0))
        val stringBuilder = StringBuilder()
        val decimalFormat = DecimalFormat("#,###.#")
        val power = 1024.0.pow(long10)
        stringBuilder.append(decimalFormat.format(d/power).toDouble())
        stringBuilder.append(" ")
        stringBuilder.append(arrayOf("B", "KB", "MB", "GB", "TB")[long10.toInt()])
        return stringBuilder.toString()
    }
}