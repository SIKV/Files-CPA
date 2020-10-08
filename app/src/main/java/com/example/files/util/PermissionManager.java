package com.example.files.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

public class PermissionManager {

    private static final int RC_MANAGE_ALL_FILES_ACCESS_PERMISSION = 100;
    private static final int RC_READ_EXTERNAL_STORAGE_PERMISSION = 101;
    private static final int RC_OPEN_APP_SETTINGS = 102;

    public interface Listener {
        void onStoragePermissionGranted();
        void onStoragePermissionDenied();
    }

    private Activity activity;
    private Listener listener;

    public PermissionManager(@NotNull Activity activity, @NotNull Listener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalStoragePermission();
        } else {
            requestReadExternalStoragePermission();
        }
    }

    public void grantStoragePermissionManually() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalStoragePermission();
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);

            activity.startActivityForResult(intent, RC_OPEN_APP_SETTINGS);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener.onStoragePermissionGranted();
            } else {
                listener.onStoragePermissionDenied();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_MANAGE_ALL_FILES_ACCESS_PERMISSION) {
            if (isManageExternalStoragePermissionGranted()) {
                listener.onStoragePermissionGranted();
            } else {
                listener.onStoragePermissionDenied();
            }
        } else if (requestCode == RC_OPEN_APP_SETTINGS) {
            if (isReadExternalStoragePermissionGranted()) {
                listener.onStoragePermissionGranted();
            }
        }
    }

    private boolean isReadExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadExternalStoragePermission() {
        if (isReadExternalStoragePermissionGranted()) {
            listener.onStoragePermissionGranted();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    RC_READ_EXTERNAL_STORAGE_PERMISSION
            );
        }
    }

    @TargetApi(30)
    private boolean isManageExternalStoragePermissionGranted() {
        return Environment.isExternalStorageManager();
    }

    @TargetApi(30)
    private void requestManageExternalStoragePermission() {
        if (isManageExternalStoragePermissionGranted()) {
            listener.onStoragePermissionGranted();
        } else {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivityForResult(intent, RC_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            } catch (ActivityNotFoundException e) {
                listener.onStoragePermissionDenied();
            }
        }
    }
}