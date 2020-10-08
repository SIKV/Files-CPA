package com.example.files.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.files.R;
import com.example.files.ui.adapter.FileModelAdapter;
import com.example.files.util.PermissionManager;
import com.example.files.util.Utils;
import com.example.files.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements PermissionManager.Listener {

    private ProgressBar progressBar;

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
    public void onStoragePermissionGranted() {
        observe();
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

    private void observe() {
        viewModel.getFiles().observe(this, files -> {
            filesAdapter.setItems(files);
        });

        viewModel.getGetFilesProgressState().observe(this, state -> {
            switch (state) {
                case IDLE:
                case DONE:
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    break;
                case IN_PROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);

        RecyclerView filesRecycler = findViewById(R.id.filesRecycler);
        filesRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        filesRecycler.setAdapter(filesAdapter);

        EditText findEditText = findViewById(R.id.findEditText);

        findEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // TODO Implement

                Utils.hideSoftInput(MainActivity.this);
                return true;
            }

            return false;
        });

        findViewById(R.id.saveFab).setOnClickListener(v -> {
            // TODO Implement
        });
    }
}