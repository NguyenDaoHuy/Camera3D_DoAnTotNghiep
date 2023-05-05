package com.bhsoft.ar3d.ui.activity.adapterEdit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bhsoft.ar3d.R;
import com.bhsoft.ar3d.ui.activity.edit_other.ColorCircleDrawable;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public ColorAdapter(String[] strArr, Context context) {
        this.listItem = strArr;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (Build.VERSION.SDK_INT < 16) {
            viewHolder.image.setBackgroundDrawable(new ColorCircleDrawable(Color.parseColor(this.listItem[i])));
        } else {
            viewHolder.image.setBackground(new ColorCircleDrawable(Color.parseColor(this.listItem[i])));
        }
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
