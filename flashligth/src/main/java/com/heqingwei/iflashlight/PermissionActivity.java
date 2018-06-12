package com.heqingwei.iflashlight;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/4/19.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    private void checkPermission() {
        final int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            toHome();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    1);
        }
    }

    private void toHome() {
        startActivity(new Intent(this, IFLMainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请允许权限，否则无法打开应用程序", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            toHome();
        }
    }
}
