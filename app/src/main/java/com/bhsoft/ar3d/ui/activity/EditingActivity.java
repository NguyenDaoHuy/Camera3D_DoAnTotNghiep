package com.bhsoft.ar3d.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsoft.ar3d.R;
import com.bhsoft.ar3d.StickerNew.StickerView;
import com.bhsoft.ar3d.databinding.ConfirmSaveImageBinding;
import com.bhsoft.ar3d.databinding.EditLayoutBinding;
import com.bhsoft.ar3d.ui.activity.adapterEdit.ColorAdapter;
import com.bhsoft.ar3d.ui.activity.adapterEdit.ColorAdapter2;
import com.bhsoft.ar3d.ui.activity.adapterEdit.EffectAdapter;
import com.bhsoft.ar3d.ui.activity.adapterEdit.FontAdapter;
import com.bhsoft.ar3d.ui.activity.adapterEdit.GalleryAdapter2;
import com.bhsoft.ar3d.ui.activity.edit_other.BitmapLoader;
import com.bhsoft.ar3d.ui.activity.edit_other.BitmapProcessing;
import com.bhsoft.ar3d.ui.activity.edit_other.DisplayUtil;
import com.bhsoft.ar3d.ui.activity.edit_other.Util;
import com.bhsoft.ar3d.ui.utils.Utility;
import com.commit451.nativestackblur.NativeStackBlur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import ke.tang.ruler.OnRulerValueChangeListener;
import me.grantland.widget.AutofitTextView;


public class EditingActivity extends BaseActivity {
    public static final String DIRECTORY_PATH = "/OverlayEffect";
    public static StickerView mCurrentView;
    public static ArrayList<View> mViews = new ArrayList<>();
    private AutofitTextView afltext;
    private boolean blurFinished;
    private Bitmap bmblur;
    private Bitmap bmmain;
    private Bitmap bmmask;
    private int center;
    private boolean checkTouch;
    private ColorAdapter2 colorAdapter2;
    private String colorsample;
    private int dis;
    private EditText edtext;
    private EffectAdapter effectAdapter;
    private String fontsample;
    private com.bhsoft.ar3d.ui.activity.adapterEdit.GalleryAdapter2 GalleryAdapter2;
    private GPUImageView gpuview;
    int[] iArr;
    private ImageView icabc;
    private LinearLayout icadjust;
    private LinearLayout iceffect;
    private LinearLayout icframe;
    private ImageView icrandom;
    private LinearLayout icsnap;
    private LinearLayout ictext;
    private boolean isNext;
    private ImageView ivalign;
    private ImageView ivblur;
    private ImageView ivchangecontrast;
    private ImageView ivchangeexposure;
    private ImageView ivchangehighlight;
    private ImageView ivchangeshadow;
    private ImageView ivchangesharpen;
    private ImageView ivchangetemperature;
    private ImageView ivchangevignette;
    private ImageView ivcircle;
    private ImageView ivframe;
    private int lastTouchedPositionX;
    private int lastTouchedPositionY;
    private String[] listItem;
    private String[] listfont;
    private LinearLayout llchange;
    private LinearLayout llcontrol;
    private int mToolbarColor;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    private MenuItem menuItemCrop;
    private Drawable menuItemCropIconDone;
    private Drawable menuItemCropIconSave;
    private RelativeLayout rlblur;
    private RelativeLayout rlphoto;
    private RelativeLayout rltext;
    private RecyclerView rvselect;
    private RecyclerView rvtext;
    private String savePath;
    private String textsample;
    private long timetouch;
    private TextView toolbarTitle;
    private TextView tvslider;
    private int widthScreen;
    private int wthumb;
    private int contrast = 0;
    private int exposure = 0;
    private String filter = "galaxys/thumb_effect_00000.jpg";
    private boolean firstTouch = false;
    private int highlight = 0;
    private int kindEdit = 0;
    private String[] listColor = {"#ffffff", "#d4d4d4", "#828282", "#515A5A", "#444444", "#000000", "#873600", "#9C640C", "#9A7D0A", "#1D8348", "#0E6655", "#1A5276", "#2980B9", "#5B2C6F", "#F08080", "#7B241C", "#CB4335", "#e60012", "#499157", "#f44444", "#d38f23", "#0099cc", "#f9d1fa", "#c3e2cc", "#50e3c2", "#f24c4c", "#ffa0f5", "#3ebde0", "#f8d9f7", "#e3ac55", "#00cc9e", "#cc5200", "#8b00cc", "#cc008b", "#a3cc00"};
    private boolean mShowLoader = true;
    private int precontrast = 0;
    private int preexposure = 0;
    private int prehighlight = 0;
    private int preshadow = 0;
    private int presharpen = 0;
    private int pretemperature = 0;
    private int prevignette = 0;
    private int shadow = 0;
    private int sharpen = 0;
    private int temperature = 0;
    private int type = 0;
    private int vignette = 0;
    String imageSavePath;
    private TextView titleEdit;
    private EditLayoutBinding binding;
    private boolean checkSetLayoutSelect = true;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = EditLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.isNext = false;
        imageSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + getResources().getString(R.string.app_name);
        this.widthScreen = DisplayUtil.getDisplayWidthPixels(this);
        this.bmmain = Utility.easer_bitmap;
        Bitmap bitmap = this.bmmain;

        if (bitmap == null) {
            try {
                Toast.makeText(this, "Couldn't handle this image, It has large size!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.rlphoto = (RelativeLayout) findViewById(R.id.rlphoto);
            this.rlphoto.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    EditingActivity.this.resetSticker();
                    binding.confirmAddSticker.setVisibility(View.GONE);
                    return false;
                }
            });

            if (binding.rlphoto.getWidth() == 0 || binding.rlphoto.getHeight() == 0) {
                binding.rlphoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.rlphoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        // Tính toán tỉ lệ và cập nhật ảnh tại đây
                        int w = binding.rlphoto.getWidth();
                        int h = binding.rlphoto.getHeight();
                        int bitmapWidth = bmmain.getWidth();
                        int bitmapHeight = bmmain.getHeight();
                        float bitmapRatio = (float) bitmapWidth / (float) bitmapHeight;
                        float recyclerViewRatio  = (float) w / (float) h;
                        int newWidth, newHeight;
                        if (bitmapRatio > recyclerViewRatio) {
                            newWidth = binding.rlphoto.getWidth();
                            newHeight = (int) (newWidth / bitmapRatio);
                        } else {
                            newHeight = binding.rlphoto.getHeight();
                            newWidth = (int) (newHeight * bitmapRatio);
                        }
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlphoto.getLayoutParams();
                        layoutParams.height = newHeight;
                        layoutParams.width = newWidth;
                        //layoutParams.addRule(13);
                        rlphoto.setLayoutParams(layoutParams);
                    }
                });
            } else {

            }

            this.gpuview = (GPUImageView) findViewById(R.id.gpuview);
            this.gpuview.setImage(bmmain);
            this.blurFinished = true;
            this.rvselect = (RecyclerView) findViewById(R.id.rvselect);
            this.ivframe = (ImageView) findViewById(R.id.ivframe);
            this.ivframe.setTag("0");
            this.rvselect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            if (Util.IS_DISPLAY_ADS) {
                boolean z = Util.IS_DEBUG;
            }
            this.icframe = (LinearLayout) findViewById(R.id.icframe);   // frame
            this.icframe.setOnClickListener(new C02541());
            this.icadjust = (LinearLayout) findViewById(R.id.icadjust);  //adjstment  , kind = 3
            this.icadjust.setOnClickListener(new C02552());
            this.iceffect = (LinearLayout) findViewById(R.id.iceffect);  //filter
            this.iceffect.setOnClickListener(new C02563());
            this.icsnap = (LinearLayout) findViewById(R.id.icsnap);    //sticker
            this.icsnap.setOnClickListener(new C02574());
            this.ictext = (LinearLayout) findViewById(R.id.ictext);  //text , kind = 1
            this.ictext.setOnClickListener(new C02607());
            this.icabc = (ImageView) findViewById(R.id.icabc);
            this.icabc.setOnClickListener(new C02585());
            this.icrandom = (ImageView) findViewById(R.id.icrandom);
            this.icrandom.setOnClickListener(new C02596());
            this.rltext = (RelativeLayout) findViewById(R.id.rltext);
            this.rvtext = (RecyclerView) findViewById(R.id.rvtext);
            this.afltext = (AutofitTextView) findViewById(R.id.afltext);
            this.edtext = (EditText) findViewById(R.id.edtext);
            this.titleEdit = findViewById(R.id.titleEdittext);

            // Edit text options
            ((ImageView) findViewById(R.id.ivchangetext)).setOnClickListener(new C02618());
            this.edtext.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
            this.edtext.addTextChangedListener(new C02629());
            ((ImageView) findViewById(R.id.ivchangefont)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.rvtext.setVisibility(View.VISIBLE);
                    EditingActivity.this.closeKeyboard();
                    EditingActivity.this.rvtext.setLayoutManager(new GridLayoutManager(EditingActivity.this, 3));
                    EditingActivity.this.loadFont();
                    setColorEditTextOptions(binding.ivchangefont);
                }
            });

            ((ImageView) findViewById(R.id.ivchangecolor)).setOnClickListener(new View.OnClickListener() {
                class C03991 implements ColorAdapter.OnRecyclerViewItemClickListener {
                    C03991() {
                    }

                    @Override
                    public void onItemClick(View view, String str) {
                        EditingActivity.this.loadSampleText(str, "", "");
                    }
                }

                @Override
                public void onClick(View view) {
                    EditingActivity.this.rvtext.setVisibility(View.VISIBLE);
                    //     EditingActivity.this.edtext.setVisibility(View.GONE);
                    EditingActivity.this.closeKeyboard();
                    EditingActivity.this.rvtext.setLayoutManager(new GridLayoutManager(EditingActivity.this, 6));
                    ColorAdapter colorAdapter = new ColorAdapter(EditingActivity.this.listColor, EditingActivity.this);
                    EditingActivity.this.rvtext.setAdapter(colorAdapter);
                    colorAdapter.setOnItemClickListener(new C03991());
                    setColorEditTextOptions(binding.ivchangecolor);
                }
            });
            this.ivalign = (ImageView) findViewById(R.id.ivalign);
            this.ivalign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.setAlignText();
                    setColorEditTextOptions(binding.ivalign);
                }
            });
            this.ivcircle = (ImageView) findViewById(R.id.ivcircle);
            this.ivcircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.setCircleText();
                    setColorEditTextOptions(binding.ivcircle);
                }
            });
            this.rlblur = (RelativeLayout) findViewById(R.id.rlblur);
            this.ivblur = (ImageView) findViewById(R.id.ivblur);
            this.ivblur.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    EditingActivity.this.lastTouchedPositionX = (int) motionEvent.getX();
                    EditingActivity.this.lastTouchedPositionY = (int) motionEvent.getY();
          //          EditingActivity.this.refreshImageView();
                    return true;
                }
            });


            //Adjustment
            this.ivchangeexposure = (ImageView) findViewById(R.id.ivchangeexposure);
            this.ivchangecontrast = (ImageView) findViewById(R.id.ivchangecontrast);
            this.ivchangesharpen = (ImageView) findViewById(R.id.ivchangesharpen);
            this.ivchangetemperature = (ImageView) findViewById(R.id.ivchangetemperature);
            this.ivchangehighlight = (ImageView) findViewById(R.id.ivchangehighlight);
            this.ivchangeshadow = (ImageView) findViewById(R.id.ivchangeshadow);
            this.ivchangevignette = (ImageView) findViewById(R.id.ivchangevignette);
            this.llcontrol = (LinearLayout) findViewById(R.id.llcontrol);
            this.llchange = (LinearLayout) findViewById(R.id.llchange);
            this.tvslider = (TextView) findViewById(R.id.tvslider);
            loadPointforSlider();

            this.ivchangeexposure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.isNetworkAvailable(EditingActivity.this.getApplicationContext());
                    EditingActivity.this.type = 1;
                    setValueMaxMin(100, -100, exposure);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.exposure);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangecontrast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.type = 2;
                    setValueMaxMin(100, 0, contrast);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.contrast);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangesharpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.isNetworkAvailable(EditingActivity.this.getApplicationContext());
                    EditingActivity.this.type = 3;
                    setValueMaxMin(100, 0, sharpen);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.sharpen);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangehighlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.type = 4;
                    setValueMaxMin(100, 0, highlight);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.highlight);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangeshadow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.isNetworkAvailable(EditingActivity.this.getApplicationContext());
                    EditingActivity.this.type = 5;
                    setValueMaxMin(100, 0, shadow);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.shadow);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangetemperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditingActivity.this.type = 6;
                    setValueMaxMin(100, -100, temperature);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.temperature);
                    EditingActivity.this.setChange();
                }
            });
            this.ivchangevignette.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.isNetworkAvailable(EditingActivity.this.getApplicationContext());
                    EditingActivity.this.type = 7;
                    setValueMaxMin(100, 0, vignette);
                    EditingActivity editingActivity = EditingActivity.this;
                    editingActivity.setTextSlider(editingActivity.vignette);
                    EditingActivity.this.setChange();
                }
            });
            loadFrame();
            onClickButton();
        }
    }


    class C02541 implements View.OnClickListener {
        C02541() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.frames));
            EditingActivity.this.loadFrame();
            setRecyclerViewSticker(false);
            binding.rvselect.setVisibility(View.VISIBLE);
        }
    }

    class C02552 implements View.OnClickListener {
        C02552() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.kindEdit = 3;
            EditingActivity.this.type = 1;
            EditingActivity editingActivity = EditingActivity.this;
            editingActivity.setTextSlider(editingActivity.exposure);
            setValueMaxMin(100, -100, exposure);
            EditingActivity.this.setChange();
            EditingActivity.this.llchange.setVisibility(View.VISIBLE);
            binding.rlslider.setVisibility(View.VISIBLE);
            EditingActivity.this.clickable(false);
            rvselect.setVisibility(View.GONE);
        }
    }
    class C02563 implements View.OnClickListener {
        C02563() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.galaxy));
            EditingActivity.this.loadGalaxy();
            setRecyclerViewSticker(false);
            binding.rvselect.setVisibility(View.VISIBLE);
        }
    }


    class C02574 implements View.OnClickListener {
        C02574() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.stickers));
            setRecyclerViewSticker(true);
            EditingActivity.this.loadSnap();
            binding.rvselect.setVisibility(View.VISIBLE);
        }
    }


    class C02585 implements View.OnClickListener {
        C02585() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.captions));
            EditingActivity.this.loadABC();
        }
    }


    class C02596 implements View.OnClickListener {
        C02596() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.random));
            //    EditingActivity.this.gpuEffect();
        }
    }

    class C02607 implements View.OnClickListener {
        C02607() {
        }

        @Override
        public void onClick(View view) {
            EditingActivity.this.kindEdit = 1;
            EditingActivity.this.titleEdit.setText(EditingActivity.this.getResources().getString(R.string.addtext));
            EditingActivity.this.rltext.setVisibility(View.VISIBLE);
            EditingActivity.this.rvtext.setVisibility(View.GONE);
            EditingActivity.this.edtext.setVisibility(View.VISIBLE);
            EditingActivity.this.edtext.requestFocus();
            EditingActivity.this.afltext.setText("");
            EditingActivity.this.edtext.setText("");
            EditingActivity.this.ivalign.setTag(1);
            EditingActivity.this.ivalign.setImageResource(R.drawable.ic_textalign_center);
            EditingActivity.this.ivcircle.setTag(0);
            EditingActivity.this.ivcircle.setImageResource(R.drawable.iccircle);
            EditingActivity.this.afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
            EditingActivity.this.clickable(false);
            EditingActivity.this.loadSampleText("#ffffff", Util.FONT_MAIN, "");
            ((InputMethodManager) EditingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(EditingActivity.this.edtext, 1);
            setColorEditTextOptions(binding.ivchangetext);
            binding.rvselect.setVisibility(View.GONE);
            edtext.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        onWindowFocusChanged(false);
                        // Xử lý sự kiện nút back ở đây
                        if (edtext.isFocused()) {
                            edtext.clearFocus();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }


    class C02618 implements View.OnClickListener {
        C02618() {
        }

        @Override
        public void onClick(View view) {
            setColorEditTextOptions(binding.ivchangetext);
            EditingActivity.this.rvtext.setVisibility(View.GONE);
            EditingActivity.this.edtext.setVisibility(View.VISIBLE);
            EditingActivity.this.edtext.requestFocus();
            ((InputMethodManager) EditingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(EditingActivity.this.edtext, 1);
        }
    }


    class C02629 implements TextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C02629() {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            EditingActivity.this.afltext.setText(charSequence.toString());
            if (binding.edtext.getText().toString().trim().isEmpty()) {
                binding.afltext.setText(R.string.edit_example);
            }
        }
    }


    public class BlurTask extends AsyncTask<Void, Void, Bitmap> {
        private int maskPosX;
        private int maskPosY;

        public BlurTask(int i, int i2) {
            this.maskPosX = i;
            this.maskPosY = i2;
        }

        @Override
        protected void onPreExecute() {
            EditingActivity.this.blurFinished = false;
        }


        public Bitmap doInBackground(Void... voidArr) {
            try {
                Bitmap applyMask = BitmapProcessing.applyMask(EditingActivity.this.bmmain, EditingActivity.this.bmmask, this.maskPosX, this.maskPosY);
                EditingActivity.this.bmblur = NativeStackBlur.process(EditingActivity.this.bmmain, 8);
                new Canvas(EditingActivity.this.bmblur).drawBitmap(applyMask, (float) this.maskPosX, (float) this.maskPosY, new Paint());
                applyMask.recycle();
                return EditingActivity.this.bmblur;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            EditingActivity.this.ivblur.setImageBitmap(bitmap);
            EditingActivity.this.blurFinished = true;
        }
    }


    public class saveAndGo extends AsyncTask<Void, Void, String> {

        public String doInBackground(Void... voidArr) {
            return "";
        }

        String image;
        saveAndGo(String image) {
            this.image = image;
        }

        @Override
        protected void onPreExecute() {
            EditingActivity.this.mShowLoader = true;
        }

        public void onPostExecute(String str) {
            EditingActivity editingActivity = EditingActivity.this;
            editingActivity.savePath = editingActivity.savePhoto(image);
            EditingActivity.this.mShowLoader = false;
            if (EditingActivity.this.savePath.equals("")) {
                Toast.makeText(EditingActivity.this, "Couldn't save photo, error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onClickButton() {
        binding.btnDone.setOnClickListener(v -> {
            onClickSave();
        });

        binding.btnCancel.setOnClickListener(v -> {
            onBack();
        });

        binding.btnCancelEdText.setOnClickListener(v -> {
            onCancelTexture();
        });

        binding.btnDoneEdText.setOnClickListener(v -> {
            onSaveTexture();
        });

        binding.btnCancelAdjustment.setOnClickListener(v -> {
            onCancelAdjustment();
        });

        binding.btnDoneAdjustment.setOnClickListener(v -> {
            onSaveAdjustment();
        });
    }


    public void setAlignText() {
        if (this.ivalign.getTag().equals(1)) {
            this.afltext.setGravity(3);
            this.ivalign.setImageResource(R.drawable.edittext_textalign_justifyleft);
            this.ivalign.setTag(2);
        } else if (this.ivalign.getTag().equals(2)) {
            this.afltext.setGravity(5);
            this.ivalign.setImageResource(R.drawable.ic_textalign_right);
            this.ivalign.setTag(3);
        } else if (this.ivalign.getTag().equals(3)) {
            this.afltext.setGravity(17);
            this.ivalign.setImageResource(R.drawable.ic_textalign_center);
            this.ivalign.setTag(1);
        }
    }

    public void setCircleText() {
        if (this.ivcircle.getTag().equals(0)) {
            this.ivcircle.setImageResource(R.drawable.iccirclepressed);
            this.ivcircle.setTag(1);
            this.afltext.setShadowLayer(1.6f, 4.0f, 4.0f, -1);
        } else if (this.ivcircle.getTag().equals(1)) {
            this.ivcircle.setTag(0);
            this.ivcircle.setImageResource(R.drawable.iccircle);
            this.afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
        }
    }

    private void setClick() {
        this.GalleryAdapter2.setOnItemClickListener(new GalleryAdapter2.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String str) {
                if (EditingActivity.this.rltext.getVisibility() == View.GONE
                        && EditingActivity.this.rlblur.getVisibility() == View.GONE
                        && EditingActivity.this.llchange.getVisibility() == View.GONE) {
                    if (str.contains("frame_")) {
                        EditingActivity.this.addFrame(str);
                    } else if (str.contains("sticker_")) {
                        EditingActivity.this.addStickerItem(str);
                    } else if (str.contains("text_")) {
                        EditingActivity.this.addABC(str);
                    } else if (str.contains("emoji_")) {
                        EditingActivity.this.addStickerItem(str);
                    } else if (str.contains("effect_")) {
                        EditingActivity.this.filter = str;
                        EditingActivity.this.ivframe.setTag("0");
                  //      EditingActivity.this.gpuEffect();
                        gpuview.setFilter(setAdjustment());
                    }
                }
            }
        });
    }


    public void effectClick() {
        this.effectAdapter.setOnItemClickListener(new EffectAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String str) {
                if (EditingActivity.this.rltext.getVisibility() == View.GONE
                        && EditingActivity.this.rlblur.getVisibility() == View.GONE
                        && EditingActivity.this.llchange.getVisibility() == View.GONE
                        && str.contains("thumb_effect_")) {
              //      EditingActivity.this.filter = str;
                    //         EditingActivity.this.gpuEffect();
                }
            }
        });
    }

    @Override
    @SuppressLint({"PrivateResource"})
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
        MenuItem findItem = menu.findItem(R.id.menu_loader);
        Drawable icon = findItem.getIcon();
        if (icon != null) {
            try {
                icon.mutate();
                icon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                findItem.setIcon(icon);
            } catch (IllegalStateException e) {
                Log.i("Photos to Collage", e.getMessage());
            }
            ((Animatable) findItem.getIcon()).start();
        }
        this.menuItemCrop = menu.findItem(R.id.menu_crop);
        this.menuItemCrop.setIcon(this.menuItemCropIconSave);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!this.mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(this.mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Toast.makeText(this, "onOptionsItemSelected", Toast.LENGTH_SHORT).show();
        Exception e;
        int i = this.kindEdit;
        if (i == 0) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                this.isNext = true;
                //clickSave();
            } else if (menuItem.getItemId() == 16908332) {
                this.isNext = false;
                finish();
            }
        } else if (i == 1) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                this.textsample = this.afltext.getText().toString();
                if (!this.textsample.equals("")) {
                    this.afltext.setDrawingCacheEnabled(true);
                    this.afltext.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    Bitmap createBitmap = Bitmap.createBitmap(this.afltext.getDrawingCache());
                    this.afltext.setDrawingCacheEnabled(false);
                    int i2 = 0;
                    boolean z = false;
                    while (i2 < this.rlphoto.getChildCount()) {
                        if (i2 >= this.rlphoto.getChildCount()) {
                            break;
                        }
                        try {
                            if ((this.rlphoto.getChildAt(i2) instanceof com.bhsoft.ar3d.ui.activity.edit_other.StickerView) && ((com.bhsoft.ar3d.ui.activity.edit_other.StickerView) this.rlphoto.getChildAt(i2)).isEdit()) {
                                try {
                                    initStickerBitmap(createBitmap);
                                    z = true;
                                    break;
                                } catch (Exception e2) {
                                    e = e2;
                                    z = true;
                                    Log.i("Photos to Collage", e.getMessage());
                                    i2++;
                                }
                            }
                        } catch (Exception e3) {
                            e = e3;
                        }
                        i2++;
                    }
                    if (!z) {
                        addText(createBitmap);
                    }
                }
            } else if (menuItem.getItemId() == 16908332) {
                resetEditSticker();
            }
            this.rltext.setVisibility(View.GONE);
            this.kindEdit = 0;
            this.titleEdit.setText(getResources().getString(R.string.editphoto));
            clickable(true);
            closeKeyboard();
        } else if (i == 2) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                this.gpuview.setImage(this.bmblur);
            }
            this.rlblur.setVisibility(View.GONE);
            this.ivblur.setVisibility(View.GONE);
            this.kindEdit = 0;
            this.titleEdit.setText(getResources().getString(R.string.editphoto));
            clickable(true);
        } else if (i == 3) {

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            int i2 = this.kindEdit;
            if (i2 == 0) {
                this.isNext = false;
                onBack();
            } else if (i2 == 1) {
                resetEditSticker();
                this.kindEdit = 0;
                this.rltext.setVisibility(View.GONE);
                this.titleEdit.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                return false;
            } else if (i2 == 2) {
                this.kindEdit = 0;
                this.rlblur.setVisibility(View.GONE);
                this.ivblur.setVisibility(View.GONE);
                this.titleEdit.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                return false;
            } else if (i2 == 3) {
                this.exposure = this.preexposure;
                this.contrast = this.precontrast;
                this.highlight = this.prehighlight;
                int i3 = this.preshadow;
                this.shadow = i3;
                this.sharpen = i3;
                this.temperature = this.pretemperature;
                this.vignette = this.prevignette;
                this.kindEdit = 0;
                this.type = 0;
                this.llcontrol.setVisibility(View.VISIBLE);
                this.rvselect.setVisibility(View.VISIBLE);
                this.llchange.setVisibility(View.GONE);
                binding.rlslider.setVisibility(View.GONE);
                this.titleEdit.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                return false;
            }
        }
        if(i == KeyEvent.KEYCODE_BACK){
            onWindowFocusChanged(false);
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void clickSave(String image) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_write_storage_rationale), 102);
        } else {
            new saveAndGo(image).execute(new Void[0]);
        }
    }

    public String savePhoto(String imageName) {
        return loadSave(imageName);
    }

    private void addImageGallery(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", str);
        contentValues.put("mime_type", "image/jpeg");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }


    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 102) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        } else if (iArr[0] == 0) {

        }
    }

    //Add sticker
    public void addStickerItem(String str) {
        Bitmap bitmap;
        com.bhsoft.ar3d.ui.activity.edit_other.StickerView stickerView = new com.bhsoft.ar3d.ui.activity.edit_other.StickerView((Context) this, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoader.loadFromAsset(this, new int[]{720, 720}, str.replace("thumb_", ""));
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        initStickerBitmap(bitmap);
    }

    private void initStickerBitmap(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(bitmap);
        int width = bitmap.getWidth();
        int width2 = bitmap.getWidth();
        Log.d("Tag", "width" + width);
        Log.d("Tag", "height" + width2);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                EditingActivity.mViews.remove(stickerView);
                EditingActivity.this.rlphoto.removeView(stickerView);
                binding.confirmAddSticker.setVisibility(View.GONE);
            }

            @Override
            public void onEdit(StickerView stickerView2) {
                EditingActivity.mCurrentView.setInEdit(false);
                EditingActivity.mCurrentView = stickerView2;
                EditingActivity.mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView2) {
                int indexOf = EditingActivity.mViews.indexOf(stickerView2);
                if (indexOf != EditingActivity.mViews.size() - 1) {
                    EditingActivity.mViews.add(EditingActivity.mViews.size(), (StickerView) EditingActivity.mViews.remove(indexOf));
                }
            }
        });
        this.rlphoto.addView(stickerView, new RelativeLayout.LayoutParams(-1, -1));
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
        binding.confirmAddSticker.setVisibility(View.VISIBLE);
        binding.btnDoneSticker.setOnClickListener(v -> {
            EditingActivity.this.resetSticker();
            binding.confirmAddSticker.setVisibility(View.GONE);
        });
        binding.btnCancelSticker.setOnClickListener(v -> {
            EditingActivity.mViews.remove(stickerView);
            EditingActivity.this.rlphoto.removeView(stickerView);
            binding.confirmAddSticker.setVisibility(View.GONE);
        });
    }

    private void setCurrentEdit(StickerView stickerView) {
        StickerView stickerView2 = mCurrentView;
        if (stickerView2 != null) {
            stickerView2.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    public void resetSticker() {
        StickerView stickerView = mCurrentView;
        if (stickerView != null) {
            stickerView.setInEdit(false);
        }
    }

    public void addFrame(String str) {
        if (str.contains("00000")) {
            this.ivframe.setImageBitmap(null);
        } else {
            this.ivframe.setImageBitmap(BitmapLoader.loadFromAsset(this, new int[]{1440, 1440}, str.replace("thumb_", "")));
        }
    }

    public void addABC(String str) {
        Bitmap bitmap;
        com.bhsoft.ar3d.ui.activity.edit_other.StickerView stickerView = new com.bhsoft.ar3d.ui.activity.edit_other.StickerView((Context) this, true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoader.loadFromAsset(this, new int[]{720, 720}, str.replace("thumb_", ""));
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        initStickerBitmap(bitmap);
    }

    private void addSnap(String str) {
        Bitmap bitmap;
        com.bhsoft.ar3d.ui.activity.edit_other.StickerView stickerView = new com.bhsoft.ar3d.ui.activity.edit_other.StickerView((Context) this, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoader.loadFromAsset(this, new int[]{512, 512}, str);
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(str);
    }

    private void addText(Bitmap bitmap) {
        com.bhsoft.ar3d.ui.activity.edit_other.StickerView stickerView = new com.bhsoft.ar3d.ui.activity.edit_other.StickerView((Context) this, true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, layoutParams);
        stickerView.setTag("text");
        stickerView.setColor(this.colorsample);
        stickerView.setFont(this.fontsample);
        stickerView.setText(this.textsample);
        stickerView.setAlign(((Integer) this.ivalign.getTag()).intValue());
        stickerView.setCircle(((Integer) this.ivcircle.getTag()).intValue());
        initStickerBitmap(bitmap);
    }

    private void resetStickersFocus() {
        for (int i = 0; i < this.rlphoto.getChildCount(); i++) {
            try {
                if (this.rlphoto.getChildAt(i) instanceof com.bhsoft.ar3d.ui.activity.edit_other.StickerView) {
                    this.rlphoto.getChildAt(i).setFocusable(false);
                }
            } catch (Exception e) {
                Log.i("Photos to Collage", e.getMessage());
            }
        }
    }

    private void resetEditSticker() {
        for (int i = 0; i < this.rlphoto.getChildCount(); i++) {
            try {
                if (this.rlphoto.getChildAt(i) instanceof com.bhsoft.ar3d.ui.activity.edit_other.StickerView) {
                    ((com.bhsoft.ar3d.ui.activity.edit_other.StickerView) this.rlphoto.getChildAt(i)).setEdit(false);
                }
            } catch (Exception e) {
                Log.i("Photos to Collage", e.getMessage());
            }
        }
    }

    //Setup SeekBar
    private void loadPointforSlider() {
        binding.rulerPicker.setOnRulerValueChangeListener(new OnRulerValueChangeListener() {
            @Override
            public void onRulerValueChanged(int i, @NonNull String s) {
                try{
                    Boolean.valueOf(false);
                    binding.tvslider.setText(s + "%");
                    if (EditingActivity.this.type == 1) {
                        EditingActivity.this.exposure = (int) (i);
                    } else if (EditingActivity.this.type == 2) {
                        EditingActivity.this.contrast = (int) (i);
                    } else if (EditingActivity.this.type == 3) {
                        EditingActivity.this.sharpen = (int) (i);
                    } else if (EditingActivity.this.type == 4) {
                        EditingActivity.this.highlight = (int) (i);
                    } else if (EditingActivity.this.type == 5) {
                        EditingActivity.this.shadow = (int) (i);
                    } else if (EditingActivity.this.type == 6) {
                        EditingActivity.this.temperature = (int) (i);
                    } else if (EditingActivity.this.type == 7) {
                        EditingActivity.this.vignette = (int) (i);
                    }
                    gpuview.setFilter(setAdjustment());
                }catch (Exception e){
                    Log.e("Adjustment",e.getMessage());
                }
            }
        });
    }

    public void setTextSlider(int i) {
        try {
            if (this.type == 1) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 2) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 3) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 4) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 5) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 6) {
                this.tvslider.setText(String.valueOf(i) + "%");
            } else if (this.type == 7) {
                this.tvslider.setText(String.valueOf(i) + "%");
            }
        } catch (Exception unused) {
        }
    }

    public void loadFrame() {
        try {
            this.listItem = getAssets().list("frames");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            ArrayList arrayList = new ArrayList();
            String[] strArr = this.listItem;
            for (String str : strArr) {
                if (str.contains("thumb_")) {
                    arrayList.add("frames/" + str);
                }
            }
            this.listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.GalleryAdapter2 = new GalleryAdapter2(this.listItem, this, 1);
            this.rvselect.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            this.rvselect.setAdapter(this.GalleryAdapter2);
            setClick();
        }
    }

    public void loadABC() {
        try {
            this.listItem = getAssets().list("texts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            ArrayList arrayList = new ArrayList();
            String[] strArr = this.listItem;
            for (String str : strArr) {
                if (str.contains("thumb_")) {
                    arrayList.add("texts/" + str);
                }
            }
            this.listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.GalleryAdapter2 = new GalleryAdapter2(this.listItem, this, 1);
            this.rvselect.setAdapter(this.GalleryAdapter2);
            setClick();
        }
    }

    public void loadSnap() {
        try {
            this.listItem = getAssets().list("stickers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            ArrayList arrayList = new ArrayList();
            String[] strArr = this.listItem;
            for (String str : strArr) {
                if (str.contains("thumb_")) {
                    arrayList.add("stickers/" + str);
                }
            }
            this.listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.GalleryAdapter2 = new GalleryAdapter2(this.listItem, this, 2);
            this.rvselect.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false));
            this.rvselect.setAdapter(this.GalleryAdapter2);
            setClick();
        }
    }

    public void loadGalaxy() {
        try {
            this.listItem = getAssets().list("galaxys");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            ArrayList arrayList = new ArrayList();
            String[] strArr = this.listItem;
            for (String str : strArr) {
                if (str.contains("thumb_")) {
                    arrayList.add("galaxys/" + str);
                }
            }
            this.listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.GalleryAdapter2 = new GalleryAdapter2(this.listItem, this, 1);
            this.rvselect.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            this.rvselect.setAdapter(this.GalleryAdapter2);
            setClick();
        }
    }

    private void loadEffect() {
        try {
            this.listItem = getAssets().list("effects");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            ArrayList arrayList = new ArrayList();
            String[] strArr = this.listItem;
            for (String str : strArr) {
                if (str.contains("thumb_")) {
                    arrayList.add("effects/" + str);
                }
            }
            this.listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.rvselect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= 16) {
                        EditingActivity.this.rvselect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        EditingActivity.this.rvselect.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    EditingActivity editingActivity = EditingActivity.this;
                    String[] strArr2 = editingActivity.listItem;
                    EditingActivity editingActivity2 = EditingActivity.this;
                    editingActivity.effectAdapter = new EffectAdapter(strArr2, editingActivity2, editingActivity2.rvselect.getHeight());
                    EditingActivity.this.rvselect.setAdapter(EditingActivity.this.effectAdapter);
                    EditingActivity.this.effectClick();
                }
            });
        }
    }

    public void loadFont() {
        try {
            this.listfont = getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listfont != null) {
            for (int i = 0; i < this.listfont.length; i++) {
                this.listfont[i] = "fonts/" + this.listfont[i];
            }
            FontAdapter fontAdapter = new FontAdapter(this.listfont, this);
            this.rvtext.setAdapter(fontAdapter);
            fontAdapter.setOnItemClickListener(new FontAdapter.OnRecyclerViewItemClickListener() {
                public void onItemClick(View view, String resId) {
                    loadSampleText("", resId, "");
                }
            });
        }


    }

    public void loadSampleText(String str, String str2, String str3) {
        if (!str3.equals("")) {
            this.afltext.setText(str3);
            this.edtext.setText(str3);
            this.textsample = str3;
        }
        if (!str.equals("")) {
            this.afltext.setTextColor(Color.parseColor(str));
            this.colorsample = str;
        }
        if (!str2.equals("")) {
            this.afltext.setTypeface(Typeface.createFromAsset(getAssets(), str2));
            this.fontsample = str2;
        }
    }

    public void closeKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        onWindowFocusChanged(false);
    }

    public void clickable(boolean z) {
        this.icframe.setClickable(z);
        this.iceffect.setClickable(z);
        this.icsnap.setClickable(z);
        this.ictext.setClickable(z);
        this.icabc.setClickable(z);
        this.icrandom.setClickable(z);
        this.icadjust.setClickable(z);
        this.rlphoto.setClickable(z);
    }

    public void refreshImageView() {
        int i = this.lastTouchedPositionX;
        if (i != -1 && this.lastTouchedPositionY != -1 && this.blurFinished) {
            this.lastTouchedPositionX = i - (this.bmmask.getWidth() / 2);
            this.lastTouchedPositionY -= this.bmmask.getHeight() / 2;
            if (this.lastTouchedPositionX < 0) {
                this.lastTouchedPositionX = 0;
            }
            if (this.lastTouchedPositionY < 0) {
                this.lastTouchedPositionY = 0;
            }
            if (this.lastTouchedPositionX > this.bmmain.getWidth()) {
                this.lastTouchedPositionX = this.bmmain.getWidth() - 10;
            }
            if (this.lastTouchedPositionY > this.bmmain.getHeight()) {
                this.lastTouchedPositionY = this.bmmain.getHeight() - 10;
            }
            new BlurTask(this.lastTouchedPositionX, this.lastTouchedPositionY).execute(new Void[0]);
        }
    }

    public void gpuEffect() {
        try {
            Boolean.valueOf(false);
            GPUImageFilterGroup gPUImageFilterGroup = new GPUImageFilterGroup();
            if (this.exposure != 0) {
                Boolean.valueOf(true);
                float f = 2.0f - ((((float) (exposure)) * 0.01f) + 1.0f);
                GPUImageGammaFilter gPUImageGammaFilter = new GPUImageGammaFilter();
                gPUImageGammaFilter.setGamma(f);
                gPUImageFilterGroup.addFilter(gPUImageGammaFilter);
            }
            if (this.contrast != 0) {
                Boolean.valueOf(true);
                GPUImageContrastFilter gPUImageContrastFilter = new GPUImageContrastFilter();  // 0-->4 : 1
                gPUImageContrastFilter.setContrast((((float) this.contrast) * 0.1f) + 1.0f);
                gPUImageFilterGroup.addFilter(gPUImageContrastFilter);
            }
            if (this.sharpen != 0) {
                Boolean.valueOf(true);
                GPUImageSharpenFilter gPUImageSharpenFilter = new GPUImageSharpenFilter(); // -4-->4 : 0
                gPUImageSharpenFilter.setSharpness(((float) this.sharpen) * 0.1f);
                gPUImageFilterGroup.addFilter(gPUImageSharpenFilter);
            }
            if (!(this.highlight == 0 && this.shadow == 0)) {
                Boolean.valueOf(true);
                GPUImageHighlightShadowFilter gPUImageHighlightShadowFilter = new GPUImageHighlightShadowFilter(); // hightlight : 0-->1 : 0
                gPUImageHighlightShadowFilter.setHighlights(1.0f - (((float) this.highlight) * 0.08f));            // shadown : 0-->1 : 1
                gPUImageHighlightShadowFilter.setShadows(((float) this.shadow) * 0.08f);
                gPUImageFilterGroup.addFilter(gPUImageHighlightShadowFilter);
            }
            if (this.temperature != 0) {
                Boolean.valueOf(true);
                int i = 400;
                if (this.temperature < 6) {
                    i = 200;
                }
                GPUImageWhiteBalanceFilter gPUImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
                gPUImageWhiteBalanceFilter.setTemperature(((float) ((this.temperature - 6) * i)) + 5000.0f); // 0-->3 : 1
                gPUImageFilterGroup.addFilter(gPUImageWhiteBalanceFilter);
            }
            if (this.vignette != 0) {
                Boolean.valueOf(true);
                PointF pointF = new PointF();
                pointF.x = 0.5f;
                pointF.y = 0.5f;
                float[] fArr = new float[3];  // 0-->3 : 1
                gPUImageFilterGroup.addFilter(new GPUImageVignetteFilter(pointF, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 1.0f - (((float) this.vignette) * 0.01f)));
            }
            if (this.ivframe.getTag().toString().equals("0")) {
                GPUImageScreenBlendFilter gPUImageScreenBlendFilter = new GPUImageScreenBlendFilter();
                this.iArr = new int[2];
                gPUImageScreenBlendFilter.setBitmap(BitmapLoader.loadFromAsset(this, new int[]{512, 512}, this.filter.replace("thumb_", "")));
                gPUImageFilterGroup.addFilter(gPUImageScreenBlendFilter);
                this.ivframe.setTag("1");
            }
            this.gpuview.setFilter(gPUImageFilterGroup);
        } catch (Exception unused) {

        }
    }

    //save image
    private String loadSave(String imageName) {
        EditingActivity.this.resetSticker();
        Bitmap bitmapWithFilterApplied = this.gpuview.getGPUImage().getBitmapWithFilterApplied();
        binding.ivphoto.setVisibility(View.VISIBLE);
        binding.ivphoto.setImageBitmap(bitmapWithFilterApplied);
        this.rlphoto.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(this.rlphoto.getDrawingCache());
        this.rlphoto.setDrawingCacheEnabled(false);
        binding.ivphoto.setVisibility(View.INVISIBLE);
        Utility.share_ = createBitmap;
        new File(this.imageSavePath).mkdirs();
        String str = this.imageSavePath;
        Utility.shareFile = new File(str, imageName + ".png");
        String valueOf = String.valueOf(Utility.shareFile);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Utility.shareFile);
            Utility.share_.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), SaveSuccessfulActivity.class);
        intent.putExtra("imagePath", valueOf);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        return valueOf;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Change layout Adjustment
    public void setChange() {
        try {
            if (this.type == 1) {
                this.titleEdit.setText(getResources().getString(R.string.editexposure));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.editexposure));
                setChangeColorIconAjustment(binding.ivchangeexposure);
            } else if (this.type == 2) {
                this.titleEdit.setText(getResources().getString(R.string.editcontrast));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.editcontrast));
                setChangeColorIconAjustment(binding.ivchangecontrast);
            } else if (this.type == 3) {
                this.titleEdit.setText(getResources().getString(R.string.editsharpen));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.editsharpen));
                setChangeColorIconAjustment(binding.ivchangesharpen);
            } else if (this.type == 4) {
                this.titleEdit.setText(getResources().getString(R.string.edithightlightsave));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.edithightlightsave));
                setChangeColorIconAjustment(binding.ivchangehighlight);
            } else if (this.type == 5) {
                this.titleEdit.setText(getResources().getString(R.string.editshadowsave));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.editshadowsave));
                setChangeColorIconAjustment(binding.ivchangeshadow);
            } else if (this.type == 6) {
                this.titleEdit.setText(getResources().getString(R.string.edittemperature));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.edittemperature));
                setChangeColorIconAjustment(binding.ivchangetemperature);
            } else if (this.type == 7) {
                this.titleEdit.setText(getResources().getString(R.string.editvignette));
                binding.tvAdjustmentType.setText(getResources().getString(R.string.editexposure));
                setChangeColorIconAjustment(binding.ivchangevignette);
            }
        } catch (Exception | OutOfMemoryError unused) {
        }
    }

    void onSaveTexture() {
        this.textsample = this.afltext.getText().toString();
        if (!this.textsample.equals("")) {
            this.afltext.setDrawingCacheEnabled(true);
            this.afltext.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap createBitmap = Bitmap.createBitmap(this.afltext.getDrawingCache());
            this.afltext.setDrawingCacheEnabled(false);
            int i2 = 0;
            boolean z = false;
            while (i2 < this.rlphoto.getChildCount()) {
                if (i2 >= this.rlphoto.getChildCount()) {
                    break;
                }
                try {
                    if ((this.rlphoto.getChildAt(i2) instanceof com.bhsoft.ar3d.ui.activity.edit_other.StickerView) && ((com.bhsoft.ar3d.ui.activity.edit_other.StickerView) this.rlphoto.getChildAt(i2)).isEdit()) {
                        try {
                            initStickerBitmap(createBitmap);
                            z = true;
                            break;
                        } catch (Exception e2) {
                            z = true;
                            Log.i("Photos to Collage", e2.getMessage());
                            i2++;
                        }
                    }
                } catch (Exception e3) {

                }
                i2++;
            }
            if (!z) {
                addText(createBitmap);
            }
        }
        this.rltext.setVisibility(View.GONE);
        this.kindEdit = 0;
        this.titleEdit.setText(getResources().getString(R.string.editphoto));
        clickable(true);
        closeKeyboard();
    }

    void onCancelTexture() {
        resetEditSticker();
        this.kindEdit = 0;
        this.rltext.setVisibility(View.GONE);
        this.titleEdit.setText(getResources().getString(R.string.editphoto));
        clickable(true);
    }

    void onSaveAdjustment() {
        this.preexposure = this.exposure;
        this.precontrast = this.contrast;
        this.prehighlight = this.highlight;
        this.preshadow = this.shadow;
        this.presharpen = this.sharpen;
        this.pretemperature = this.temperature;
        this.prevignette = this.vignette;
        this.llcontrol.setVisibility(View.VISIBLE);
        this.rvselect.setVisibility(View.VISIBLE);
        this.llchange.setVisibility(View.GONE);
        binding.rlslider.setVisibility(View.GONE);
        this.kindEdit = 0;
        this.type = 0;
        this.titleEdit.setText(getResources().getString(R.string.editphoto));
        clickable(true);
    }

    void onCancelAdjustment() {
        this.exposure = this.preexposure;
        this.contrast = this.precontrast;
        this.highlight = this.prehighlight;
        this.shadow = preshadow;
        this.sharpen = presharpen;
        this.temperature = this.pretemperature;
        this.vignette = this.prevignette;
        resetPreset();
        this.kindEdit = 0;
        this.type = 0;
        this.llcontrol.setVisibility(View.VISIBLE);
        this.rvselect.setVisibility(View.VISIBLE);
        this.llchange.setVisibility(View.GONE);
        binding.rlslider.setVisibility(View.GONE);
        this.titleEdit.setText(getResources().getString(R.string.editphoto));
        clickable(true);
    }

    void setColorEditTextOptions(ImageView img) {
        binding.ivchangetext.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.ivchangefont.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.ivchangecolor.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.ivcircle.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.ivalign.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        img.setColorFilter(ContextCompat.getColor(EditingActivity.this, R.color.blue_crop), PorterDuff.Mode.SRC_IN);
    }

    void setChangeColorIconAjustment(ImageView img) {
        ivchangeexposure.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangecontrast.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangesharpen.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangetemperature.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangehighlight.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangeshadow.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        ivchangevignette.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button));
        img.setBackground(AppCompatResources.getDrawable(EditingActivity.this, R.drawable.custom_adjustment_button_checked));
    }

    void setRecyclerViewSticker(boolean type) {
        if (type && checkSetLayoutSelect) {
            ViewGroup.LayoutParams params = rvselect.getLayoutParams();
            int heightOld = params.height;
            params.height = (int) (heightOld * 1.5);
            rvselect.setLayoutParams(params);
            checkSetLayoutSelect = false;
        } else if (!type && !checkSetLayoutSelect) {
            ViewGroup.LayoutParams params = rvselect.getLayoutParams();
            int heightOld = params.height;
            params.height = (int) (heightOld / 1.5);
            rvselect.setLayoutParams(params);
            checkSetLayoutSelect = true;
        }
    }

    void setValueMaxMin(int max, int min, int value) {
//        binding.rulerPicker.setMaxValue(max);
//        binding.rulerPicker.setMinValue(min);
//        binding.rulerPicker.setValue(value);
    }

    void resetPreset(){
        Boolean.valueOf(false);
        GPUImageFilterGroup gPUImageFilterGroup = new GPUImageFilterGroup();
        if (this.exposure != 0) {
            Boolean.valueOf(true);
            float f = 2.0f - ((((float) (exposure)) * 0.01f) + 1.0f);   // i=0 ; f=1
            GPUImageGammaFilter gPUImageGammaFilter = new GPUImageGammaFilter();
            gPUImageGammaFilter.setGamma(f);
            gPUImageFilterGroup.addFilter(gPUImageGammaFilter);
        }else if(this.exposure == 0){
            float f = 2.0f - ((((float) (0)) * 0.01f) + 1.0f);
            GPUImageGammaFilter gPUImageGammaFilter = new GPUImageGammaFilter();
            gPUImageGammaFilter.setGamma(f);
            gPUImageFilterGroup.addFilter(gPUImageGammaFilter);
        }
        if (this.contrast != 0) {
            Boolean.valueOf(true);
            GPUImageContrastFilter gPUImageContrastFilter = new GPUImageContrastFilter();  // 0-->4 : 1
            gPUImageContrastFilter.setContrast((((float) this.contrast) * 0.1f) + 1.0f);   // i=0 ;f=1
            gPUImageFilterGroup.addFilter(gPUImageContrastFilter);
        }else if(this.contrast == 0){
            GPUImageContrastFilter gPUImageContrastFilter = new GPUImageContrastFilter();  // 0-->4 : 1
            gPUImageContrastFilter.setContrast((((float) 0) * 0.1f) + 1.0f);
            gPUImageFilterGroup.addFilter(gPUImageContrastFilter);
        }
        if (this.sharpen != 0) {
            Boolean.valueOf(true);
            GPUImageSharpenFilter gPUImageSharpenFilter = new GPUImageSharpenFilter(); // -4-->4 : 0
            gPUImageSharpenFilter.setSharpness(((float) this.sharpen) * 0.1f);     // i=0 ; f=0
            gPUImageFilterGroup.addFilter(gPUImageSharpenFilter);
        }else if(this.sharpen == 0){
            GPUImageSharpenFilter gPUImageSharpenFilter = new GPUImageSharpenFilter(); // -4-->4 : 0
            gPUImageSharpenFilter.setSharpness(((float) 0) * 0.1f);
            gPUImageFilterGroup.addFilter(gPUImageSharpenFilter);
        }
        if (!(this.highlight == 0 && this.shadow == 0)) {
            Boolean.valueOf(true);
            GPUImageHighlightShadowFilter gPUImageHighlightShadowFilter = new GPUImageHighlightShadowFilter(); // hightlight : 0-->1 : 0
            gPUImageHighlightShadowFilter.setHighlights(1.0f - (((float) this.highlight) * 0.08f));   //i=0 ;f=1         // shadown : 0-->1 : 1
            gPUImageHighlightShadowFilter.setShadows(((float) this.shadow) * 0.08f);   // i = 0 ; f = 0
            gPUImageFilterGroup.addFilter(gPUImageHighlightShadowFilter);
        }else if(this.highlight == 0 && this.shadow == 0){
            GPUImageHighlightShadowFilter gPUImageHighlightShadowFilter = new GPUImageHighlightShadowFilter(); // hightlight : 0-->1 : 0
            gPUImageHighlightShadowFilter.setHighlights(1.0f - (((float) 0) * 0.08f));           // shadown : 0-->1 : 1
            gPUImageHighlightShadowFilter.setShadows(((float) 0) * 0.08f);
            gPUImageFilterGroup.addFilter(gPUImageHighlightShadowFilter);
        }
        if (this.temperature != 0) {
            Boolean.valueOf(true);
            int i = 400;
            if (this.temperature < 6) {
                i = 200;
            }
            GPUImageWhiteBalanceFilter gPUImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
            gPUImageWhiteBalanceFilter.setTemperature(((float) ((this.temperature - 6) * i)) + 5000.0f); // 0-->3 : 1
            gPUImageFilterGroup.addFilter(gPUImageWhiteBalanceFilter);
        }else if(this.temperature == 0){
            GPUImageWhiteBalanceFilter gPUImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
            gPUImageWhiteBalanceFilter.setTemperature(5000.0f); // 0-->3 : 1
            gPUImageFilterGroup.addFilter(gPUImageWhiteBalanceFilter);
        }
        if (this.vignette != 0) {
            Boolean.valueOf(true);
            PointF pointF = new PointF();
            pointF.x = 0.5f;
            pointF.y = 0.5f;
            float[] fArr = new float[3];  // 0-->3 : 1
            gPUImageFilterGroup.addFilter(new GPUImageVignetteFilter(pointF, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 1.0f - (((float) this.vignette) * 0.01f)));
        }

        if(filter != "galaxys/thumb_effect_00000.jpg"){
            GPUImageScreenBlendFilter gPUImageScreenBlendFilter = new GPUImageScreenBlendFilter();
            this.iArr = new int[2];
            gPUImageScreenBlendFilter.setBitmap(BitmapLoader.loadFromAsset(this, new int[]{512, 512}, this.filter.replace("thumb_", "")));
            gPUImageFilterGroup.addFilter(gPUImageScreenBlendFilter);
        }
        this.gpuview.setFilter(gPUImageFilterGroup);
    }

    public GPUImageFilterGroup setAdjustment() {
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(new GPUImageGammaFilter(2.0f - ((((float) (exposure)) * 0.01f) + 1.0f)));
        filterGroup.addFilter(new GPUImageContrastFilter((((float) contrast) * 0.1f) + 1.0f));
        filterGroup.addFilter(new GPUImageSharpenFilter(((float) sharpen) * 0.1f));

        float hl = 1.0f - (((float) this.highlight) * 0.08f);
        float sd = ((float) this.shadow) * 0.08f;
        filterGroup.addFilter(new GPUImageHighlightShadowFilter(sd,hl));

        int i = 400;
        if (this.temperature < 6) {
            i = 200;
        }
        filterGroup.addFilter(new GPUImageWhiteBalanceFilter(((float) ((this.temperature) * i)) + 5000.0f,0));

        PointF pointF = new PointF();
        pointF.x = 0.5f;
        pointF.y = 0.5f;
        float[] fArr = new float[3];
        filterGroup.addFilter(new GPUImageVignetteFilter(pointF, new float[]{0.0f, 0.0f, 0.0f}, 0.6f, 1.0f - (((float) this.vignette) * 0.01f)));

        String filterGoc = "galaxys/thumb_effect_00000.jpg";
        if(!filter.contains(filterGoc) && this.ivframe.getTag().toString().equals("0")){
            GPUImageScreenBlendFilter gPUImageScreenBlendFilter = new GPUImageScreenBlendFilter();
            this.iArr = new int[2];
            gPUImageScreenBlendFilter.setBitmap(BitmapLoader.loadFromAsset(this, new int[]{512, 512}, this.filter.replace("thumb_", "")));
            filterGroup.addFilter(gPUImageScreenBlendFilter);
        }else if(filter.contains(filterGoc)) {

        }
        return filterGroup;
    }

    void onClickSave(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        ConfirmSaveImageBinding dialogBinding = ConfirmSaveImageBinding.inflate(getLayoutInflater());
        alertDialogBuilder.setView(dialogBinding.getRoot());
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                  String name = dialogBinding.edImageName.getText().toString().trim();
                  if(name.isEmpty()){
                      Toast.makeText(EditingActivity.this,"Enter name", Toast.LENGTH_SHORT).show();
                  }else {
                      new saveAndGo(name).execute(new Void[0]);
                      alertDialog.dismiss();
                  }
            }
        });
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
    }

    private void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditingActivity.this);
        builder.setMessage(getResources().getString(R.string.edit_back));
        builder.setTitle(getResources().getString(R.string.message));
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
