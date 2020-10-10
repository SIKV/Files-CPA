package com.example.files.model;

import org.json.JSONException;
import org.json.JSONObject;

public class FileModel {

    private String filename;
    private String path;
    private FileAttributes attributes;

    public FileModel(String filename, String path, FileAttributes attributes) {
        this.filename = filename;
        this.path = path;
        this.attributes = attributes;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public FileAttributes getAttributes() {
        return attributes;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        try {
            json.put("filename", filename);
            json.put("path", path);
            json.put("attributes", attributes.toJson());
        } catch (JSONException e) {
            return null;
        }

        return json;
    }
}