package com.example.files.model;

import org.json.JSONException;
import org.json.JSONObject;

public class FileAttributes {

    private long creationTime;
    private long lastModifiedTime;
    private String extension;
    private long size;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        try {
            json.put("creationTime", creationTime);
            json.put("lastModifiedTime", lastModifiedTime);
            json.put("extension", extension);
            json.put("size", size);
        } catch (JSONException e) {
            return null;
        }

        return json;
    }
}