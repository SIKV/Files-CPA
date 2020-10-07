package com.example.files.model;

public class FileModel {
    private String filename;
    private String path;

    public FileModel(String filename, String path) {
        this.filename = filename;
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }
}