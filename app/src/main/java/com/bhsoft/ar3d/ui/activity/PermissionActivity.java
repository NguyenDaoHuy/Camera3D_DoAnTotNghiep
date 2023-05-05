package com.bhsoft.ar3d.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bhsoft.ar3d.databinding.ActivityPermissionBinding;
import com.bhsoft.ar3d.ui.main.MainActivity;

public class PermissionActivity extends AppCompatActivity {

    private ActivityPermissionBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_GALLERY_PERMISSION = 2;
    private boolean checkCamera = false;
    private boolean checkGallery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onClickNext();
        binding.switchPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkPermissionCamera();
                } else {

                }
            }
        });
        binding.switchPermissionGallery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkPermissionGallery();
                } else {

                }
            }
        });
    }

    private void checkPermissionCamera(){
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu cấp quyền truy cập máy ảnh
            ActivityCompat.requestPermissions(PermissionActivity.this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
            binding.switchPermission.setChecked(false);
        } else {
            // Quyền truy cập máy ảnh đã được cấp, thực hiện các hành động liên quan đến máy ảnh
            binding.switchPermission.setChecked(true);
        }
    }
    private void checkPermissionGallery(){
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PermissionActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE  }, REQUEST_GALLERY_PERMISSION);
            binding.switchPermissionGallery.setChecked(false);
        } else {
            binding.switchPermissionGallery.setChecked(true);
        }
    }

    private void onClickNext() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkCamera || !checkGallery){
                    Toast.makeText(PermissionActivity.this, "Please grant camera and gallery access", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập máy ảnh đã được cấp, thực hiện các hành động liên quan đến máy ảnh
                binding.switchPermission.setChecked(true);
                binding.switchPermission.setEnabled(false);
                checkCamera = true;
            } else {
                // Quyền truy cập máy ảnh bị từ chối, xử lý tương ứng
                binding.switchPermission.setChecked(false);
                checkCamera = false;
                displayDialogCamera();
            }
        }
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập máy ảnh đã được cấp, thực hiện các hành động liên quan đến máy ảnh
                binding.switchPermissionGallery.setChecked(true);
                binding.switchPermissionGallery.setEnabled(false);
                checkGallery = true;
            } else {
                // Quyền truy cập máy ảnh bị từ chối, xử lý tương ứng
                binding.switchPermissionGallery.setChecked(false);
                checkGallery = false;
                displayDialogGallery();
            }
        }
    }

    private void displayDialogCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
        builder.setMessage("You must grant camera permission to use the app");
        builder.setTitle("Camera Permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Go to setting", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        });
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void displayDialogGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
        builder.setMessage("You must grant gallery permission to use the app");
        builder.setTitle("Gallery Permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Go to setting", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        });
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            binding.switchPermission.setChecked(true);
            binding.switchPermission.setEnabled(false);
            checkCamera = true;
        }else {
            binding.switchPermission.setChecked(false);
            checkCamera = false;
        }
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            binding.switchPermissionGallery.setChecked(true);
            binding.switchPermissionGallery.setEnabled(false);
            checkGallery = true;
        }else {
            binding.switchPermissionGallery.setChecked(false);
            checkGallery = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }
}