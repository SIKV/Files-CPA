package com.example.files.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.files.R;
import com.example.files.model.ProgressState;
import com.example.files.ui.adapter.FileModelAdapter;
import com.example.files.util.PermissionManager;
import com.example.files.util.Utils;
import com.example.files.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements PermissionManager.Listener {

    private static final String PM_READ_FILES_TAG = "PM_READ_FILES_TAG";
    private static final String PM_SAVE_FILES_LIST_TAG = "PM_SAVE_FILES_LIST_TAG";

    private ProgressBar progressBar;
    private FloatingActionButton saveFilesListFab;

    private FileModelAdapter filesAdapter = new FileModelAdapter();

    private MainViewModel viewModel;

    private PermissionManager permissionManager = new PermissionManager(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();
        setListeners();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        permissionManager.requestStoragePermission(PM_READ_FILES_TAG);
    }

    @Override
    public void onStoragePermissionGranted(String tag) {
        switch (tag) {
            case PM_READ_FILES_TAG:
                observe();
                break;

            case PM_SAVE_FILES_LIST_TAG:
                saveFilesList();
                break;
        }
    }

    @Override
    public void onStoragePermissionDenied(String tag) {
        showStoragePermissionNotGranted(v -> permissionManager.grantStoragePermissionManually(tag));
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

        viewModel.getSaveFilesListProgressState().observe(this, state -> {
            saveFilesListFab.setEnabled(state != ProgressState.IN_PROGRESS);
        });
    }

    private void saveFilesList() {
        viewModel.saveFilesList().observe(this, filePath -> {
            String text;

            if (filePath != null) {
                text = getString(R.string.file_saved_s, filePath);
            } else {
                text = getString(R.string.error_saving_file);
            }

            Toast.makeText(this, text, Toast.LENGTH_LONG)
                    .show();
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

    private void showStoragePermissionNotGranted(View.OnClickListener listener) {
        View rootLayout = findViewById(R.id.rootLayout);

        Snackbar.make(rootLayout, R.string.storage_permission_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.grant, listener)
                .show();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);

        RecyclerView filesRecycler = findViewById(R.id.filesRecycler);
        filesRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        filesRecycler.setAdapter(filesAdapter);

        saveFilesListFab = findViewById(R.id.saveFilesListFab);
    }

    private void setListeners() {
        EditText findEditText = findViewById(R.id.findEditText);

        findEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // TODO Implement
                Utils.hideSoftInput(MainActivity.this);
                return true;
            }

            return false;
        });

        saveFilesListFab.setOnClickListener(v -> {
            permissionManager.requestStoragePermission(PM_SAVE_FILES_LIST_TAG);
        });
    }
}