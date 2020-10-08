package com.example.files.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.files.R;
import com.example.files.ui.adapter.FileModelAdapter;
import com.example.files.util.PermissionManager;
import com.example.files.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements PermissionManager.Listener {

    private FileModelAdapter filesAdapter = new FileModelAdapter();

    private MainViewModel viewModel;

    private PermissionManager permissionManager = new PermissionManager(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        permissionManager.requestStoragePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        permissionManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStoragePermissionGranted() {
        viewModel.getFiles().observe(this, files -> {
            filesAdapter.setItems(files);
        });
    }

    @Override
    public void onStoragePermissionDenied() {
        View rootLayout = findViewById(R.id.rootLayout);

        Snackbar.make(rootLayout, R.string.storage_permission_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.grant, v -> {
                    permissionManager.grantStoragePermissionManually();
                })
                .show();
    }

    private void initViews() {
        RecyclerView filesRecycler = findViewById(R.id.filesRecycler);
        filesRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        filesRecycler.setAdapter(filesAdapter);
    }
}