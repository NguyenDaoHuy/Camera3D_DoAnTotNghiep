package com.bhsoft.ar3d.ui.activity.adapterEdit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsoft.ar3d.R;
import com.bhsoft.ar3d.ui.activity.edit_other.BitmapLoader;
import com.bhsoft.ar3d.ui.activity.edit_other.DisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private String[] listItem;
    private Typeface typeface;
    private int width;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<String> listColor = new ArrayList(Arrays.asList("#5508c8", "#3f97e9", "#e4c120", "#c82970", "#e49820", "#949494", "#e46f20", "#ba0bc0", "#3fa3c3", "#3fc3b2", "#3fc390", "#d2434d", "#7fa31e", "#3f6ec3", "#8f08c8", "#a3951e", "#c00b98", "#1ea340"));

    
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public RelativeLayout rlcontext;
        public TextView text;

        public ViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.rlcontext = (RelativeLayout) view.findViewById(R.id.rlcontext);
            this.text = (TextView) view.findViewById(R.id.text);
        }
    }

    public EffectAdapter(String[] strArr, Context context, int i) {
        this.listItem = strArr;
        this.context = context;
        this.width = i - DisplayUtil.dip2px(this.context, 15.0f);
        this.typeface = Typeface.createFromAsset(this.context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effect, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.image.setImageBitmap(BitmapLoader.loadFromAsset(this.context, new int[]{200, 200}, this.listItem[i]));
        int i2 = this.width;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i2, i2 / 4);
        layoutParams.addRule(12);
        viewHolder.rlcontext.setLayoutParams(layoutParams);
        RelativeLayout relativeLayout = viewHolder.rlcontext;
        List<String> list = this.listColor;
        relativeLayout.setBackgroundColor(Color.parseColor(list.get(i % list.size())));
        viewHolder.itemView.setTag(this.listItem[i]);
        if (i == 0) {
            viewHolder.text.setText("NO");
        } else {
            TextView textView = viewHolder.text;
            textView.setText("E" + i);
        }
        viewHolder.text.setTypeface(this.typeface);
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
