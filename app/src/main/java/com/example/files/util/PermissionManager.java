package com.example.files.util;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;

import org.jetbrains.annotations.NotNull;

public class PermissionManager {

    public interface Listener {
        public void onStoragePermissionGranted();
        public void onStoragePermissionDenied();
    }

    private Context context;
    private Listener listener;

    public PermissionManager(@NotNull Context context, @NotNull Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void requestStoragePermission() {
        // TODO Implement
    }

    @TargetApi(30)
    private void requestManageExternalStoragePermission() {
        if (Environment.isExternalStorageManager()) {
            listener.onStoragePermissionGranted();
        } else {
            try {
                context.startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            } catch (ActivityNotFoundException e) {
                listener.onStoragePermissionDenied();
            }
        }
    }
}