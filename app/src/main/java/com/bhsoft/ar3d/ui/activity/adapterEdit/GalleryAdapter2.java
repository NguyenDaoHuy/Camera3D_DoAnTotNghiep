package com.bhsoft.ar3d.ui.activity.adapterEdit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsoft.ar3d.R;
import com.bhsoft.ar3d.ui.activity.edit_other.BitmapLoader;

public class GalleryAdapter2 extends RecyclerView.Adapter<GalleryAdapter2.ViewHolder> implements View.OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int type;


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.item_sticker);
        }
    }

    public GalleryAdapter2(String[] strArr, Context context, int type) {
        this.listItem = strArr;
        this.context = context;
        this.type = type;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = null;
        if(type == 1){
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);
        }else if(type == 2){
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageBitmap(BitmapLoader.loadFromAsset(this.context, new int[]{200, 200}, this.listItem[i]));
        viewHolder.itemView.setTag(this.listItem[i]);
    }

    public void onClick(View view) {
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = this.mOnItemClickListener;
        if (onRecyclerViewItemClickListener != null) {
            onRecyclerViewItemClickListener.onItemClick(view, (String) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mOnItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override 
    public int getItemCount() {
        return this.listItem.length;
    }
}
