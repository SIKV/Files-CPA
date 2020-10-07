package com.example.files.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.files.R;
import com.example.files.util.PermissionManager;
import com.example.files.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements PermissionManager.Listener {

    private MainViewModel viewModel;

    private PermissionManager permissionManager = new PermissionManager(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        permissionManager.requestStoragePermission();
    }

    @Override
    public void onStoragePermissionGranted() {
        // TODO Implement
    }

    @Override
    public void onStoragePermissionDenied() {
        // TODO Implement
    }
}