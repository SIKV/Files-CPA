package com.example.files.repository;

import com.example.files.model.FileModel;

import java.util.List;

public interface FilesRepository {

    List<FileModel> getAllFiles();
}