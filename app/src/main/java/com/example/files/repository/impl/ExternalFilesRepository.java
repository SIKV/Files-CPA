package com.example.files.repository.impl;

import android.os.Environment;

import com.example.files.model.FileModel;
import com.example.files.repository.FilesRepository;
import com.example.files.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ExternalFilesRepository implements FilesRepository {

    private List<FileModel> files = new ArrayList<>();

    /**
     *
     * @return the list of files from the external storage.
     */
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
                    this.files.add(new FileModel(
                            file.getName(),
                            file.getPath(),
                            FileUtils.getFileAttributes(file)
                    ));
                } else {
                    getAllFiles(file.toURI());
                }
            }
        }
    }

    /**
     * Creates a new file and saves it to the external storage directory.
     * @param filename a filename with an extension.
     * @param content string data.
     * @return the absolute path of the saved file or null if an error occurred.
     */
    @Override
    public String saveFile(String filename, String content) {
        String externalStorageDir = Environment.getExternalStorageDirectory().toString();

        File file = new File(externalStorageDir, filename);
        BufferedWriter bufferedWriter = null;

        try {
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.getAbsolutePath();
    }
}