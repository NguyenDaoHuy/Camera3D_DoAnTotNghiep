package com.bhsoft.ar3d.ui.activity.adapterEdit;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsoft.ar3d.R;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View view) {
            super(view);
            this.text = (TextView) view.findViewById(R.id.text);
        }
    }

    public FontAdapter(String[] strArr, Context context) {
        this.listItem = strArr;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_font, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.text.setTypeface(Typeface.createFromAsset(this.context.getAssets(), this.listItem[i]));
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
