package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.drag_object

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.databinding.Item3dObjectImageBinding

class Item3DAdapter(private val inters : IItem3D) : RecyclerView.Adapter<Item3DAdapter.Companion.Item3DViewModel>() {

    companion object{
        class Item3DViewModel(val binding : Item3dObjectImageBinding):RecyclerView.ViewHolder(binding.root)
    }
    interface IItem3D{
        fun getTextName() : ArrayList<String>
        fun getImagePath() : ArrayList<Int>
        fun getModelName() : ArrayList<String>
        fun getCoutPath()  :Int
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item3DViewModel {
        val binding = Item3dObjectImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Item3DViewModel(binding)
    }

    override fun onBindViewHolder(holder: Item3DViewModel, position: Int) {
        holder.binding.imageView.setImageResource(inters.getImagePath()[position])
        holder.binding.textView.text = inters.getTextName()[position]
//        holder.binding.imageView.setOnClickListener {
//            Common.model = inters.getModelName()[position]
//        }
    }

    override fun getItemCount(): Int {
        return inters.getCoutPath()
    }
}