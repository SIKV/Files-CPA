package com.example.files.repository.impl;

import android.os.Environment;

import com.example.files.model.FileModel;
import com.example.files.repository.FilesRepository;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ExternalFilesRepository implements FilesRepository {

    private List<FileModel> files = new ArrayList<>();

    @Override
    public List<FileModel> getAllFiles() {
        files.clear();

        URI entryUri = Environment.getExternalStorageDirectory().toURI();
        getAllFiles(entryUri);

        return files;
    }

    private void getAllFiles(URI uri) {
        File directory = new File(uri);

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    this.files.add(new FileModel(file.getName(), file.getPath()));
                } else {
                    getAllFiles(file.toURI());
                }
            }
        }
    }
}