package com.bhsoft.ar3d.ui.activity.edit_other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.bhsoft.ar3d.R;

public class StickerView extends View {
    public static final float MAX_SCALE_SIZE = 4.0f;
    public static final float MIN_SCALE_SIZE = 0.1f;
    private int align;
    private int circle;
    private String color;
    private boolean drawedit;
    private String font;
    private boolean isEdit;
    private Bitmap mBitmap;
    private Paint mBorderPaint;
    private RectF mContentRect;
    private Bitmap mControllerBitmap;
    private float mControllerHeight;
    private float mControllerWidth;
    private Bitmap mDeleteBitmap;
    private float mDeleteHeight;
    private float mDeleteWidth;
    private boolean mDrawController;
    private Bitmap mEditBitmap;
    private float mEditHeight;
    private float mEditWidth;
    private Bitmap mFlipBitmap;
    private float mFlipHeight;
    private float mFlipWidth;
    private boolean mInController;
    private boolean mInDelete;
    private boolean mInEdit;
    private boolean mInFlip;
    private boolean mInMove;
    private float mLastPointX;
    private float mLastPointY;
    private Matrix mMatrix;
    private OnStickerDeleteListener mOnStickerDeleteListener;
    private RectF mOriginContentRect;
    private float[] mOriginPoints;
    private Paint mPaint;
    private float[] mPoints;
    private float mScaleSize;
    private float mStickerScaleSize;
    private RectF mViewRect;
    private String text;

    
    public interface OnStickerDeleteListener {
        void onDelete(StickerView stickerView);
    }

    private void doEditSticker() {
    }

    public StickerView(Context context, boolean z) {
        this(context, (AttributeSet) null);
        setDrawedit(z);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawController = true;
        this.mStickerScaleSize = 1.0f;
        this.text = "";
        this.font = "";
        this.color = "";
        this.isEdit = false;
        this.align = 1;
        this.circle = 0;
        this.drawedit = false;
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(4.0f);
        this.mPaint.setColor(-1);
        this.mBorderPaint = new Paint(this.mPaint);
        this.mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        this.mBorderPaint.setShadowLayer((float) DisplayUtil.dip2px(getContext(), 2.0f), 0.0f, 0.0f, Color.parseColor("#33000000"));
        int displayWidthPixels = DisplayUtil.getDisplayWidthPixels(getContext());
        this.mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize);
        int i = displayWidthPixels / 12;
        this.mControllerBitmap = BitmapProcessing.resizeBitmap(this.mControllerBitmap, i, i);
        this.mControllerWidth = (float) this.mControllerBitmap.getWidth();
        this.mControllerHeight = (float) this.mControllerBitmap.getHeight();
        this.mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_deleteicon);
        this.mDeleteBitmap = BitmapProcessing.resizeBitmap(this.mDeleteBitmap, i, i);
        this.mDeleteWidth = (float) this.mDeleteBitmap.getWidth();
        this.mDeleteHeight = (float) this.mDeleteBitmap.getHeight();
        this.mFlipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flipicon);
        this.mFlipBitmap = BitmapProcessing.resizeBitmap(this.mFlipBitmap, i, i);
        this.mFlipWidth = (float) this.mFlipBitmap.getWidth();
        this.mFlipHeight = (float) this.mFlipBitmap.getHeight();
        this.mEditBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_editicon_2);
        this.mEditBitmap = BitmapProcessing.resizeBitmap(this.mEditBitmap, i, i);
        this.mEditWidth = (float) this.mEditBitmap.getWidth();
        this.mEditHeight = (float) this.mEditBitmap.getHeight();
    }

    public void setWaterMark(@NonNull Bitmap bitmap, boolean z) {
        this.mBitmap = bitmap;
        this.mStickerScaleSize = 1.0f;
        setFocusable(true);
        if (z) {
            try {
                this.mOriginPoints = new float[]{0.0f, 0.0f, (float) bitmap.getWidth(), 0.0f, (float) bitmap.getWidth(), (float) bitmap.getWidth(), 0.0f, (float) bitmap.getWidth(), ((float) this.mBitmap.getWidth()) / 2.0f, ((float) this.mBitmap.getHeight()) / 2.0f};
                this.mOriginContentRect = new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getWidth());
                this.mPoints = new float[10];
                this.mContentRect = new RectF();
                this.mMatrix = new Matrix();
                this.mMatrix.postTranslate((((float) DisplayUtil.getDisplayWidthPixels(getContext())) - ((float) this.mBitmap.getWidth())) / 2.0f, (((float) DisplayUtil.getDisplayWidthPixels(getContext())) - ((float) this.mBitmap.getHeight())) / 2.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.mOriginPoints = new float[]{0.0f, 0.0f, (float) bitmap.getWidth(), 0.0f, (float) bitmap.getWidth(), (float) bitmap.getWidth(), 0.0f, (float) bitmap.getWidth(), ((float) this.mBitmap.getWidth()) / 2.0f, ((float) this.mBitmap.getHeight()) / 2.0f};
            this.mOriginContentRect = new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getWidth());
        }
        postInvalidate();
    }

    public Matrix getMarkMatrix() {
        return this.mMatrix;
    }

    @Override 
    public void setFocusable(boolean z) {
        super.setFocusable(z);
        postInvalidate();
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        Matrix matrix;
        super.onDraw(canvas);
        if (this.mBitmap != null && (matrix = this.mMatrix) != null) {
            matrix.mapPoints(this.mPoints, this.mOriginPoints);
            this.mMatrix.mapRect(this.mContentRect, this.mOriginContentRect);
            canvas.drawBitmap(this.mBitmap, this.mMatrix, this.mPaint);
            if (this.mDrawController && isFocusable()) {
                float[] fArr = this.mPoints;
                canvas.drawLine(fArr[0], fArr[1], fArr[2], fArr[3], this.mBorderPaint);
                float[] fArr2 = this.mPoints;
                canvas.drawLine(fArr2[2], fArr2[3], fArr2[4], fArr2[5], this.mBorderPaint);
                float[] fArr3 = this.mPoints;
                canvas.drawLine(fArr3[4], fArr3[5], fArr3[6], fArr3[7], this.mBorderPaint);
                float[] fArr4 = this.mPoints;
                canvas.drawLine(fArr4[6], fArr4[7], fArr4[0], fArr4[1], this.mBorderPaint);
                Bitmap bitmap = this.mControllerBitmap;
                float[] fArr5 = this.mPoints;
                canvas.drawBitmap(bitmap, fArr5[4] - (this.mControllerWidth / 2.0f), fArr5[5] - (this.mControllerHeight / 2.0f), this.mBorderPaint);
                Bitmap bitmap2 = this.mDeleteBitmap;
                float[] fArr6 = this.mPoints;
                canvas.drawBitmap(bitmap2, fArr6[0] - (this.mDeleteWidth / 2.0f), fArr6[1] - (this.mDeleteHeight / 2.0f), this.mBorderPaint);
                Bitmap bitmap3 = this.mFlipBitmap;
                float[] fArr7 = this.mPoints;
                canvas.drawBitmap(bitmap3, fArr7[2] - (this.mFlipWidth / 2.0f), fArr7[3] - (this.mFlipHeight / 2.0f), this.mBorderPaint);
                if (isDrawedit()) {
                    Bitmap bitmap4 = this.mEditBitmap;
                    float[] fArr8 = this.mPoints;
                    canvas.drawBitmap(bitmap4, fArr8[6] - (this.mEditWidth / 2.0f), fArr8[7] - (this.mEditHeight / 2.0f), this.mBorderPaint);
                }
            }
        }
    }

    public Bitmap getBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.mDrawController = false;
        draw(canvas);
        this.mDrawController = true;
        canvas.save();
        return createBitmap;
    }

    public void setShowDrawController(boolean z) {
        this.mDrawController = z;
    }

    public boolean isInController(float f, float f2) {
        float[] fArr = this.mPoints;
        float f3 = fArr[4];
        float f4 = fArr[5];
        float f5 = this.mControllerWidth;
        float f6 = this.mControllerHeight;
        return new RectF(f3 - (f5 / 2.0f), f4 - (f6 / 2.0f), (f5 / 2.0f) + f3, (f6 / 2.0f) + f4).contains(f, f2);
    }

    public boolean isInDelete(float f, float f2) {
        float[] fArr = this.mPoints;
        float f3 = fArr[0];
        float f4 = fArr[1];
        float f5 = this.mDeleteWidth;
        float f6 = this.mDeleteHeight;
        return new RectF(f3 - f5, f4 - f6, f5 + f3, f6 + f4).contains(f, f2);
    }

    public boolean isInFlip(float f, float f2) {
        float[] fArr = this.mPoints;
        float f3 = fArr[2];
        float f4 = fArr[3];
        float f5 = this.mFlipWidth;
        float f6 = this.mFlipHeight;
        return new RectF(f3 - (f5 / 2.0f), f4 - (f6 / 2.0f), (f5 / 2.0f) + f3, (f6 / 2.0f) + f4).contains(f, f2);
    }

    public boolean isInEdit(float f, float f2) {
        if (!isDrawedit()) {
            return false;
        }
        float[] fArr = this.mPoints;
        float f3 = fArr[6];
        float f4 = fArr[7];
        float f5 = this.mEditWidth;
        float f6 = this.mEditHeight;
        return new RectF(f3 - (f5 / 2.0f), f4 - (f6 / 2.0f), (f5 / 2.0f) + f3, (f6 / 2.0f) + f4).contains(f, f2);
    }

    @Override 
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        if (this.mViewRect == null) {
            this.mViewRect = new RectF(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                if (!isInController(x, y)) {
                    if (!isInDelete(x, y)) {
                        if (!isInFlip(x, y)) {
                            if ((isDrawedit() && isInEdit(x, y)) || !this.mContentRect.contains(x, y)) {
                                this.mInEdit = true;
                                break;
                            } else {
                                this.mLastPointY = y;
                                this.mLastPointX = x;
                                this.mInMove = true;
                                break;
                            }
                        } else {
                            this.mInFlip = true;
                            break;
                        }
                    } else {
                        this.mInDelete = true;
                        break;
                    }
                } else {
                    this.mInController = true;
                    this.mLastPointY = y;
                    this.mLastPointX = x;
                    break;
                }
            case 1:
                if ((isInDelete(x, y) && this.mInDelete) || !isInFlip(x, y) || !this.mInFlip) {
                    doDeleteSticker();
                    break;
                } else {
                    doFlipSticker();
                    break;
                }
            case 2:
                if (this.mInController) {
                    Matrix matrix = this.mMatrix;
                    float rotation = rotation(motionEvent);
                    float[] fArr = this.mPoints;
                    matrix.postRotate(rotation, fArr[8], fArr[9]);
                    float[] fArr2 = this.mPoints;
                    float caculateLength = caculateLength(fArr2[0], fArr2[1]);
                    float caculateLength2 = caculateLength(motionEvent.getX(), motionEvent.getY());
                    float f = caculateLength - caculateLength2;
                    if (((float) Math.sqrt((double) (f * f))) > 0.0f) {
                        float f2 = caculateLength2 / caculateLength;
                        float f3 = this.mStickerScaleSize * f2;
                        if (f3 >= 0.1f && f3 <= 4.0f) {
                            Matrix matrix2 = this.mMatrix;
                            float[] fArr3 = this.mPoints;
                            matrix2.postScale(f2, f2, fArr3[8], fArr3[9]);
                            this.mStickerScaleSize = f3;
                        }
                    }
                    invalidate();
                    this.mLastPointX = x;
                    this.mLastPointY = y;
                    break;
                } else if (this.mInMove) {
                    float f4 = x - this.mLastPointX;
                    float f5 = y - this.mLastPointY;
                    this.mInController = false;
                    if (((float) Math.sqrt((double) ((f4 * f4) + (f5 * f5)))) > 2.0f && canStickerMove(f4, f5)) {
                        this.mMatrix.postTranslate(f4, f5);
                        postInvalidate();
                        this.mLastPointX = x;
                        this.mLastPointY = y;
                        break;
                    }
                } else {
                    return true;
                }
                break;
        }
        this.mLastPointX = 0.0f;
        this.mLastPointY = 0.0f;
        this.mInController = false;
        this.mInMove = false;
        this.mInDelete = false;
        this.mInFlip = false;
        this.mInEdit = false;
        return true;
    }

    private void doDeleteSticker() {
        setVisibility(GONE);
        OnStickerDeleteListener onStickerDeleteListener = this.mOnStickerDeleteListener;
        if (onStickerDeleteListener != null) {
            onStickerDeleteListener.onDelete(this);
        }
        ((ViewGroup) getParent()).removeView(this);
    }

    private void doFlipSticker() {
        this.mBitmap = BitmapProcessing.flip(this.mBitmap, true, false);
        postInvalidate();
    }

    public void changeColor(int i) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
        new Canvas(this.mBitmap).drawBitmap(this.mBitmap, 0.0f, 0.0f, paint);
        postInvalidate();
    }

    private boolean canStickerMove(float f, float f2) {
        RectF rectF = this.mViewRect;
        float[] fArr = this.mPoints;
        return rectF.contains(f + fArr[8], f2 + fArr[9]);
    }

    private float caculateLength(float f, float f2) {
        float[] fArr = this.mPoints;
        float f3 = f - fArr[8];
        float f4 = f2 - fArr[9];
        return (float) Math.sqrt((double) ((f3 * f3) + (f4 * f4)));
    }

    private float rotation(MotionEvent motionEvent) {
        return calculateDegree(motionEvent.getX(), motionEvent.getY()) - calculateDegree(this.mLastPointX, this.mLastPointY);
    }

    private float calculateDegree(float f, float f2) {
        float[] fArr = this.mPoints;
        return (float) Math.toDegrees(Math.atan2((double) (f2 - fArr[9]), (double) (f - fArr[8])));
    }

    public RectF getContentRect() {
        return this.mContentRect;
    }

    public String getFont() {
        return this.font;
    }

    public void setFont(String str) {
        this.font = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String str) {
        this.color = str;
    }

    public boolean isEdit() {
        return this.isEdit;
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
    }

    public int getAlign() {
        return this.align;
    }

    public void setAlign(int i) {
        this.align = i;
    }

    public int getCircle() {
        return this.circle;
    }

    public void setCircle(int i) {
        this.circle = i;
    }

    public boolean isDrawedit() {
        return this.drawedit;
    }

    public void setDrawedit(boolean z) {
        this.drawedit = z;
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener onStickerDeleteListener) {
        this.mOnStickerDeleteListener = onStickerDeleteListener;
    }
}
