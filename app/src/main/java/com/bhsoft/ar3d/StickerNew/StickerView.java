package com.bhsoft.ar3d.StickerNew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.core.view.MotionEventCompat;

import com.bhsoft.ar3d.R;

public class StickerView extends ImageView {
    private static final float BITMAP_SCALE = 0.7f;
    private static final String TAG = "StickerView";
    private Bitmap deleteBitmap;
    private int deleteBitmapHeight;
    private int deleteBitmapWidth;
    private DisplayMetrics dm;
    private Rect dst_delete;
    private Rect dst_flipV;
    private Rect dst_resize;
    private Rect dst_top;
    private Bitmap flipVBitmap;
    private int flipVBitmapHeight;
    private int flipVBitmapWidth;
    private double halfDiagonalLength;
    private boolean isInSide;
    private float lastLength;
    private float lastRotateDegree;
    private float lastX;
    private float lastY;
    private Paint localPaint;
    private Bitmap mBitmap;
    private int mScreenHeight;
    private int mScreenwidth;
    private float oldDis;
    private OperationListener operationListener;
    private Bitmap resizeBitmap;
    private int resizeBitmapHeight;
    private int resizeBitmapWidth;
    private Bitmap topBitmap;
    private int topBitmapHeight;
    private int topBitmapWidth;
    private PointF mid = new PointF();
    private boolean isPointerDown = false;
    private final float pointerLimitDis = 20.0f;
    private final float pointerZoomCoeff = 0.09f;
    private boolean isInResize = false;
    private Matrix matrix = new Matrix();
    private boolean isInEdit = true;
    private float MIN_SCALE = 0.5f;
    private float MAX_SCALE = 1.2f;
    private float oringinWidth = 0.0f;
    private boolean isHorizonMirror = false;
    private final long stickerId = 0;

    
    public interface OperationListener {
        void onDeleteClick();

        void onEdit(StickerView stickerView);

        void onTop(StickerView stickerView);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public StickerView(Context context) {
        super(context);
        init();
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.dst_delete = new Rect();
        this.dst_resize = new Rect();
        this.dst_flipV = new Rect();
        this.dst_top = new Rect();
        this.localPaint = new Paint();
        this.localPaint.setColor(getResources().getColor(R.color.black));
        this.localPaint.setAntiAlias(true);
        this.localPaint.setDither(true);
        this.localPaint.setStyle(Paint.Style.STROKE);
        this.localPaint.setStrokeWidth(2.0f);
        this.dm = getResources().getDisplayMetrics();
        this.mScreenwidth = this.dm.widthPixels;
        this.mScreenHeight = this.dm.heightPixels;
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            float[] fArr = new float[9];
            this.matrix.getValues(fArr);
            float f = fArr[2] + (fArr[0] * 0.0f) + (fArr[1] * 0.0f);
            float f2 = (fArr[3] * 0.0f) + (fArr[4] * 0.0f) + fArr[5];
            float width = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * 0.0f) + fArr[2];
            float width2 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * 0.0f) + fArr[5];
            float height = (fArr[0] * 0.0f) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
            float height2 = (fArr[3] * 0.0f) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
            float width3 = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
            float width4 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
            canvas.save();
            canvas.drawBitmap(this.mBitmap, this.matrix, null);
            Rect rect = this.dst_delete;
            int i = this.deleteBitmapWidth;
            rect.left = (int) (width - ((float) (i / 6)));
            rect.right = (int) (((float) (i / 6)) + width);
            int i2 = this.deleteBitmapHeight;
            rect.top = (int) (width2 - ((float) (i2 / 6)));
            rect.bottom = (int) (((float) (i2 / 6)) + width2);
            Rect rect2 = this.dst_resize;
            int i3 = this.resizeBitmapWidth;
            rect2.left = (int) (width3 - ((float) (i3 / 6)));
            rect2.right = (int) (width3 + ((float) (i3 / 6)));
            int i4 = this.resizeBitmapHeight;
            rect2.top = (int) (width4 - ((float) (i4 / 6)));
            rect2.bottom = (int) (((float) (i4 / 6)) + width4);
            Rect rect3 = this.dst_top;
            int i5 = this.flipVBitmapWidth;
            rect3.left = (int) (f - ((float) (i5 / 6)));
            rect3.right = (int) (((float) (i5 / 6)) + f);
            int i6 = this.flipVBitmapHeight;
            rect3.top = (int) (f2 - ((float) (i6 / 6)));
            rect3.bottom = (int) (((float) (i6 / 6)) + f2);
            Rect rect4 = this.dst_flipV;
            int i7 = this.topBitmapWidth;
            rect4.left = (int) (height - ((float) (i7 / 6)));
            rect4.right = (int) (((float) (i7 / 6)) + height);
            int i8 = this.topBitmapHeight;
            rect4.top = (int) (height2 - ((float) (i8 / 6)));
            rect4.bottom = (int) (((float) (i8 / 6)) + height2);
            if (this.isInEdit) {
                canvas.drawLine(f, f2, width, width2, this.localPaint);
                canvas.drawLine(width, width2, width3, width4, this.localPaint);
                canvas.drawLine(height, height2, width3, width4, this.localPaint);
                canvas.drawLine(height, height2, f, f2, this.localPaint);
                canvas.drawBitmap(this.deleteBitmap, (Rect) null, this.dst_delete, (Paint) null);
                canvas.drawBitmap(this.resizeBitmap, (Rect) null, this.dst_resize, (Paint) null);
                canvas.drawBitmap(this.flipVBitmap, (Rect) null, this.dst_flipV, (Paint) null);
                canvas.drawBitmap(this.topBitmap, (Rect) null, this.dst_top, (Paint) null);
            }
            canvas.restore();
        }
    }

    @Override 
    public void setImageResource(int i) {
        setBitmap(BitmapFactory.decodeResource(getResources(), i));
    }

    public void setBitmap(Bitmap bitmap) {
        this.matrix.reset();
        this.mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int width = this.mBitmap.getWidth();
        int height = this.mBitmap.getHeight();
        this.oringinWidth = (float) width;
        float f = (this.MIN_SCALE + this.MAX_SCALE) / 2.0f;
        int i = width / 2;
        int i2 = height / 2;
        this.matrix.postScale(f, f, (float) i, (float) i2);
        Matrix matrix = this.matrix;
        int i3 = this.mScreenwidth;
        matrix.postTranslate((float) ((i3 / 2) - i), (float) ((i3 / 2) - i2));
        invalidate();
    }

    private void setDiagonalLength() {
        this.halfDiagonalLength = Math.hypot((double) this.mBitmap.getWidth(), (double) this.mBitmap.getHeight()) / 2.0d;
    }

    private void initBitmaps() {
        if (this.mBitmap.getWidth() >= this.mBitmap.getHeight()) {
            float f = (float) (this.mScreenwidth / 8);
            if (((float) this.mBitmap.getWidth()) < f) {
                this.MIN_SCALE = 1.0f;
            } else {
                this.MIN_SCALE = (f * 1.0f) / ((float) this.mBitmap.getWidth());
            }
            int width = this.mBitmap.getWidth();
            int i = this.mScreenwidth;
            if (width > i) {
                this.MAX_SCALE = 1.0f;
            } else {
                this.MAX_SCALE = (((float) i) * 1.0f) / ((float) this.mBitmap.getWidth());
            }
        } else {
            float f2 = (float) (this.mScreenwidth / 8);
            if (((float) this.mBitmap.getHeight()) < f2) {
                this.MIN_SCALE = 1.0f;
            } else {
                this.MIN_SCALE = (f2 * 1.0f) / ((float) this.mBitmap.getHeight());
            }
            int height = this.mBitmap.getHeight();
            int i2 = this.mScreenwidth;
            if (height > i2) {
                this.MAX_SCALE = 1.0f;
            } else {
                this.MAX_SCALE = (((float) i2) * 1.0f) / ((float) this.mBitmap.getHeight());
            }
        }
        this.topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_editicon_2);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_deleteicon);
        this.flipVBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flipicon);
        this.resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize);
        this.deleteBitmapWidth = (int) (((float) this.deleteBitmap.getWidth()) * BITMAP_SCALE);
        this.deleteBitmapHeight = (int) (((float) this.deleteBitmap.getHeight()) * BITMAP_SCALE);
        this.resizeBitmapWidth = (int) (((float) this.resizeBitmap.getWidth()) * BITMAP_SCALE);
        this.resizeBitmapHeight = (int) (((float) this.resizeBitmap.getHeight()) * BITMAP_SCALE);
        this.flipVBitmapWidth = (int) (((float) this.flipVBitmap.getWidth()) * BITMAP_SCALE);
        this.flipVBitmapHeight = (int) (((float) this.flipVBitmap.getHeight()) * BITMAP_SCALE);
        this.topBitmapWidth = (int) (((float) this.topBitmap.getWidth()) * BITMAP_SCALE);
        this.topBitmapHeight = (int) (((float) this.topBitmap.getHeight()) * BITMAP_SCALE);
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OperationListener operationListener;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean z = true;
        if (actionMasked != 5) {
            switch (actionMasked) {
                case 0:
                    if (!isInButton(motionEvent, this.dst_delete)) {
                        if (!isInResize(motionEvent)) {
                            if (!isInButton(motionEvent, this.dst_flipV)) {
                                if (!isInButton(motionEvent, this.dst_top)) {
                                    if (!isInBitmap(motionEvent)) {
                                        z = false;
                                        break;
                                    } else {
                                        this.isInSide = true;
                                        this.lastX = motionEvent.getX(0);
                                        this.lastY = motionEvent.getY(0);
                                        break;
                                    }
                                } else {
                                    bringToFront();
                                    OperationListener operationListener2 = this.operationListener;
                                    if (operationListener2 != null) {
                                        operationListener2.onTop(this);
                                        break;
                                    }
                                }
                            } else {
                                PointF pointF = new PointF();
                                midDiagonalPoint(pointF);
                                this.matrix.postScale(-1.0f, 1.0f, pointF.x, pointF.y);
                                this.isHorizonMirror = !this.isHorizonMirror;
                                invalidate();
                                break;
                            }
                        } else {
                            this.isInResize = true;
                            this.lastRotateDegree = rotationToStartPoint(motionEvent);
                            midPointToStartPoint(motionEvent);
                            this.lastLength = diagonalLength(motionEvent);
                            break;
                        }
                    } else {
                        OperationListener operationListener3 = this.operationListener;
                        if (operationListener3 != null) {
                            operationListener3.onDeleteClick();
                            break;
                        }
                    }
                    break;
                case 1:
                case 3:
                    this.isInResize = false;
                    this.isInSide = false;
                    this.isPointerDown = false;
                    break;
                case 2:
                    if (!this.isPointerDown) {
                        if (!this.isInResize) {
                            if (this.isInSide) {
                                float x = motionEvent.getX(0);
                                float y = motionEvent.getY(0);
                                this.matrix.postTranslate(x - this.lastX, y - this.lastY);
                                this.lastX = x;
                                this.lastY = y;
                                invalidate();
                                break;
                            }
                        } else {
                            this.matrix.postRotate((rotationToStartPoint(motionEvent) - this.lastRotateDegree) * 2.0f, this.mid.x, this.mid.y);
                            this.lastRotateDegree = rotationToStartPoint(motionEvent);
                            float diagonalLength = diagonalLength(motionEvent) / this.lastLength;
                            double diagonalLength2 = (double) diagonalLength(motionEvent);
                            double d = this.halfDiagonalLength;
                            Double.isNaN(diagonalLength2);
                            if (diagonalLength2 / d > ((double) this.MIN_SCALE) || diagonalLength >= 1.0f) {
                                double diagonalLength3 = (double) diagonalLength(motionEvent);
                                double d2 = this.halfDiagonalLength;
                                Double.isNaN(diagonalLength3);
                                if (diagonalLength3 / d2 < ((double) this.MAX_SCALE) || diagonalLength <= 1.0f) {
                                    this.lastLength = diagonalLength(motionEvent);
                                    this.matrix.postScale(diagonalLength, diagonalLength, this.mid.x, this.mid.y);
                                    invalidate();
                                    break;
                                }
                            }
                            if (!isInResize(motionEvent)) {
                                this.isInResize = false;
                            }
                            diagonalLength = 1.0f;
                            this.matrix.postScale(diagonalLength, diagonalLength, this.mid.x, this.mid.y);
                            invalidate();
                        }
                    } else {
                        float spacing = spacing(motionEvent);
                        float f = (spacing == 0.0f || spacing < 20.0f) ? 1.0f : (((spacing / this.oldDis) - 1.0f) * 0.09f) + 1.0f;
                        float abs = (((float) Math.abs(this.dst_flipV.left - this.dst_resize.left)) * f) / this.oringinWidth;
                        if ((abs > this.MIN_SCALE || f >= 1.0f) && (abs < this.MAX_SCALE || f <= 1.0f)) {
                            this.lastLength = diagonalLength(motionEvent);
                        } else {
                            f = 1.0f;
                        }
                        this.matrix.postScale(f, f, this.mid.x, this.mid.y);
                        invalidate();
                        break;
                    }
                    break;
            }
        } else {
            if (spacing(motionEvent) > 20.0f) {
                this.oldDis = spacing(motionEvent);
                this.isPointerDown = true;
                midPointToStartPoint(motionEvent);
            } else {
                this.isPointerDown = false;
            }
            this.isInSide = false;
            this.isInResize = false;
        }
        if (z && (operationListener = this.operationListener) != null) {
            operationListener.onEdit(this);
        }
        return z;
    }

    public StickerPropertyModel calculate(StickerPropertyModel stickerPropertyModel) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = fArr[2];
        float f2 = fArr[5];
        Log.d(TAG, "tx : " + f + " ty : " + f2);
        float f3 = fArr[0];
        float f4 = fArr[3];
        float sqrt = (float) Math.sqrt((double) ((f3 * f3) + (f4 * f4)));
        Log.d(TAG, "rScale : " + sqrt);
        float round = (float) Math.round(Math.atan2((double) fArr[1], (double) fArr[0]) * 57.29577951308232d);
        Log.d(TAG, "rAngle : " + round);
        PointF pointF = new PointF();
        midDiagonalPoint(pointF);
        Log.d(TAG, " width  : " + (((float) this.mBitmap.getWidth()) * sqrt) + " height " + (((float) this.mBitmap.getHeight()) * sqrt));
        float f5 = pointF.x;
        float f6 = pointF.y;
        Log.d(TAG, "midX : " + f5 + " midY : " + f6);
        stickerPropertyModel.setDegree((float) Math.toRadians((double) round));
        stickerPropertyModel.setScaling((((float) this.mBitmap.getWidth()) * sqrt) / ((float) this.mScreenwidth));
        stickerPropertyModel.setxLocation(f5 / ((float) this.mScreenwidth));
        stickerPropertyModel.setyLocation(f6 / ((float) this.mScreenwidth));
        stickerPropertyModel.setStickerId(this.stickerId);
        if (this.isHorizonMirror) {
            stickerPropertyModel.setHorizonMirror(1);
        } else {
            stickerPropertyModel.setHorizonMirror(2);
        }
        return stickerPropertyModel;
    }

    private boolean isInBitmap(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        float f2 = (fArr[3] * 0.0f) + (fArr[4] * 0.0f) + fArr[5];
        float width = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * 0.0f) + fArr[2];
        float width2 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * 0.0f) + fArr[5];
        float height = (fArr[0] * 0.0f) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
        float height2 = (fArr[3] * 0.0f) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
        return pointInRect(new float[]{f, width, (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2], height}, new float[]{f2, width2, (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5], height2}, motionEvent.getX(0), motionEvent.getY(0));
    }

    private boolean pointInRect(float[] fArr, float[] fArr2, float f, float f2) {
        double hypot = Math.hypot((double) (fArr[0] - fArr[1]), (double) (fArr2[0] - fArr2[1]));
        double hypot2 = Math.hypot((double) (fArr[1] - fArr[2]), (double) (fArr2[1] - fArr2[2]));
        double hypot3 = Math.hypot((double) (fArr[3] - fArr[2]), (double) (fArr2[3] - fArr2[2]));
        double hypot4 = Math.hypot((double) (fArr[0] - fArr[3]), (double) (fArr2[0] - fArr2[3]));
        double hypot5 = Math.hypot((double) (f - fArr[0]), (double) (f2 - fArr2[0]));
        double hypot6 = Math.hypot((double) (f - fArr[1]), (double) (f2 - fArr2[1]));
        double hypot7 = Math.hypot((double) (f - fArr[2]), (double) (f2 - fArr2[2]));
        double hypot8 = Math.hypot((double) (f - fArr[3]), (double) (f2 - fArr2[3]));
        double d = ((hypot + hypot5) + hypot6) / 2.0d;
        double d2 = ((hypot2 + hypot6) + hypot7) / 2.0d;
        double d3 = ((hypot3 + hypot7) + hypot8) / 2.0d;
        double d4 = ((hypot4 + hypot8) + hypot5) / 2.0d;
        return Math.abs((hypot * hypot2) - (((Math.sqrt((((d - hypot) * d) * (d - hypot5)) * (d - hypot6)) + Math.sqrt((((d2 - hypot2) * d2) * (d2 - hypot6)) * (d2 - hypot7))) + Math.sqrt((((d3 - hypot3) * d3) * (d3 - hypot7)) * (d3 - hypot8))) + Math.sqrt((((d4 - hypot4) * d4) * (d4 - hypot8)) * (d4 - hypot5)))) < 0.5d;
    }

    private boolean isInButton(MotionEvent motionEvent, Rect rect) {
        int i = rect.left;
        int i2 = rect.right;
        int i3 = rect.top;
        int i4 = rect.bottom;
        if (motionEvent.getX(0) < ((float) i) || motionEvent.getX(0) > ((float) i2) || motionEvent.getY(0) < ((float) i3) || motionEvent.getY(0) > ((float) i4)) {
            return false;
        }
        return true;
    }

    private boolean isInResize(MotionEvent motionEvent) {
        int i = this.dst_resize.top - 20;
        int i2 = this.dst_resize.right + 20;
        int i3 = this.dst_resize.bottom + 20;
        if (motionEvent.getX(0) < ((float) (this.dst_resize.left - 20)) || motionEvent.getX(0) > ((float) i2) || motionEvent.getY(0) < ((float) i) || motionEvent.getY(0) > ((float) i3)) {
            return false;
        }
        return true;
    }

    private void midPointToStartPoint(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        this.mid.set(((((fArr[0] * 0.0f) + (fArr[1] * 0.0f)) + fArr[2]) + motionEvent.getX(0)) / 2.0f, ((((fArr[3] * 0.0f) + (fArr[4] * 0.0f)) + fArr[5]) + motionEvent.getY(0)) / 2.0f);
    }

    private void midDiagonalPoint(PointF pointF) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        float f2 = (fArr[3] * 0.0f) + (fArr[4] * 0.0f) + fArr[5];
        pointF.set((f + (((fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight()))) + fArr[2])) / 2.0f, (f2 + (((fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight()))) + fArr[5])) / 2.0f);
    }

    private float rotationToStartPoint(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        return (float) Math.toDegrees(Math.atan2((double) (motionEvent.getY(0) - (((fArr[3] * 0.0f) + (fArr[4] * 0.0f)) + fArr[5])), (double) (motionEvent.getX(0) - f)));
    }

    private float diagonalLength(MotionEvent motionEvent) {
        return (float) Math.hypot((double) (motionEvent.getX(0) - this.mid.x), (double) (motionEvent.getY(0) - this.mid.y));
    }

    private float spacing(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return 0.0f;
        }
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean z) {
        this.isInEdit = z;
        invalidate();
    }
}
