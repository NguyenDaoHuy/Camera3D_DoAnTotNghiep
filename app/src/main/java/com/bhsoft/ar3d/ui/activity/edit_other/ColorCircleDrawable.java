package com.bhsoft.ar3d.ui.activity.edit_other;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class ColorCircleDrawable extends Drawable {
    private final Paint mPaint = new Paint(1);
    private int mRadius = 0;

    @Override 
    public int getOpacity() {
        return -3;
    }

    public ColorCircleDrawable(int i) {
        this.mPaint.setColor(i);
    }

    @Override 
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), (float) this.mRadius, this.mPaint);
    }

    @Override 
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mRadius = Math.min(rect.width(), rect.height()) / 2;
    }

    @Override 
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    @Override 
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
