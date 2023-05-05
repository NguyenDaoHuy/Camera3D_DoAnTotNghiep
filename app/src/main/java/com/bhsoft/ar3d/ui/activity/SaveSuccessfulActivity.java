package com.bhsoft.ar3d.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.bhsoft.ar3d.R;
import com.bhsoft.ar3d.databinding.ActivitySaveSuccessfulBinding;
import com.bhsoft.ar3d.ui.main.MainActivity;
import com.bumptech.glide.Glide;

import java.io.File;

public class SaveSuccessfulActivity extends AppCompatActivity {

    private ActivitySaveSuccessfulBinding binding;
    private int REQUEST_OUT_APP = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveSuccessfulBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent() != null){
            String path = getIntent().getStringExtra("imagePath");
            File file = new File(path);
            Glide.with(this).load(file).into(binding.ivPhoto);
            binding.tvImageName.setText(file.getName());
            binding.btnShare.setOnClickListener(v -> {
                shareFile(path);
            });
        }else {
            Glide.with(this).load(R.drawable.logo_dhcnhn).into(binding.ivPhoto);
            binding.tvImageName.setText("Error Load Image");
        }
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnBackToHome.setOnClickListener(v -> {
             startActivity(new Intent(SaveSuccessfulActivity.this, MainActivity.class)
                     .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    }

    private void shareFile(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " Created By : " + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +".provider", new File(path)));
        startActivityForResult(Intent.createChooser(intent, "Share Image Using.."), REQUEST_OUT_APP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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