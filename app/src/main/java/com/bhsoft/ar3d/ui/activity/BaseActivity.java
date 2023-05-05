package com.bhsoft.ar3d.ui.activity;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity {
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private AlertDialog mAlertDialog;

    
    @Override
    
    public void onStop() {
        super.onStop();
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mAlertDialog.dismiss();
        }
    }

    protected void requestPermission(final String str, String str2, final int i) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
            showAlertDialog("Permission needed", str2, new DialogInterface.OnClickListener() {
                @Override 
                public void onClick(DialogInterface dialogInterface, int i2) {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{str}, i);
                }
            }, "OK", null, "CANCEL");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{str}, i);
        }
    }

    protected void showAlertDialog(@Nullable String str, @Nullable String str2, @Nullable DialogInterface.OnClickListener onClickListener, @NonNull String str3, @Nullable DialogInterface.OnClickListener onClickListener2, @NonNull String str4) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton(str3, onClickListener);
        builder.setNegativeButton(str4, onClickListener2);
        this.mAlertDialog = builder.show();
    }
}
