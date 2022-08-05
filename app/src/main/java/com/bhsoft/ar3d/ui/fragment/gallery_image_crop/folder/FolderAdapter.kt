package com.bhsoft.ar3d.ui.fragment.gallery_image_crop.folder

import android.text.format.Formatter
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.data.model.Pictures
import com.bhsoft.ar3d.databinding.ItemFolderBinding
import com.bhsoft.ar3d.ui.fragment.gallery_image_crop.list_image_crop.GalleryImageCropViewModel
import java.io.File
import java.util.*

class FolderAdapter(private val inter: FolderInterface) :
    RecyclerView.Adapter<FolderAdapter.ViewHolder>() {
    private var filesImageList : ArrayList<Pictures>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFolderBinding>(layoutInflater, R.layout.item_folder, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictures = inter.file(position)
        holder.binding.folderName.text = pictures!!.title
        holder.binding.folderPath.text = getImages(pictures.title).toString() + " items"
        holder.itemView.setOnClickListener { inter.onClickItem(position) }
    }

    override fun getItemCount(): Int {
        return inter.getCount()!!
    }

    class ViewHolder(var binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root)

    interface FolderInterface {
        fun getCount() : Int?
        fun file(position: Int): Pictures?
        fun onClickItem(position: Int)
    }
    fun getImages(folder:String) : Int {
        filesImageList!!.clear()
        val filePath = "/storage/emulated/0/DCIM/ObjectDetected/"
        val file = File(filePath + folder)
        val files = file.listFiles()
        if (files!=null){
            for (file1 : File in files){
                if (file1.path.endsWith(".png")||file1.path.endsWith(".jpg")){
                    filesImageList!!.add(Pictures(file1.path,file1.name,file1.length()))
                }
            }
        }
        return filesImageList!!.size
    }
}