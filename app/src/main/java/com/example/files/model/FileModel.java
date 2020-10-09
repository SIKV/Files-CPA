package com.example.files.model;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        try {
            json.put("filename", filename);
            json.put("path", path);

        } catch (JSONException e) {
            return null;
        }

        return json;
    }
}