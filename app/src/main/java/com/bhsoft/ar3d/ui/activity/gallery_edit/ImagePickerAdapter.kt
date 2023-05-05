package com.bhsoft.ar3d.ui.activity.gallery_edit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ItemImagePickerBinding
import com.bumptech.glide.Glide

class ImagePickerAdapter(var context: Context?, private val onClickListener: OnClickListener) : RecyclerView.Adapter<ImagePickerAdapter.ViewHoler>() {
    private val list: ArrayList<String> = ArrayList()
    private var select : Int = 10000
    fun setData(data: ArrayList<String>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun onClickItem(selected : Int){
        this.select = selected
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoler {
        return ViewHoler(ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHoler, position: Int) {
        if (position == 0){
            Glide.with(context!!).load(R.drawable.camera_item).into(holder.binding.imgThumbVideo)
            holder.itemView.setOnClickListener { onClickListener.onClick("", true,position) }
        }else{
            Glide.with(context!!).load(list[position]).into(holder.binding.imgThumbVideo)
            val path = list[position]
            val sb = StringBuilder()
            sb.append(context!!.applicationContext.packageName)
            sb.append(".provider")
            holder.itemView.setOnClickListener { onClickListener.onClick(path, false,position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHoler(var binding: ItemImagePickerBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onClick(path: String?,camera : Boolean, position: Int)
    }
}